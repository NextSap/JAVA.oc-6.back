package com.paymybuddy.backend.service;

import com.paymybuddy.backend.Application;
import com.paymybuddy.backend.exception.UserException;
import com.paymybuddy.backend.mapper.UserMapper;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.LoginRequest;
import com.paymybuddy.backend.object.request.SigninRequest;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.LoginResponse;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.repository.UserRepository;
import com.paymybuddy.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils = Application.getJwtUtils();
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper = UserMapper.getInstance();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        UserEntity user = userRepository.findById(email).stream().findFirst().orElseThrow(() -> new UserException.BadCredentialsException("Bad credentials"));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new UserException.BadCredentialsException("Bad credentials");

        return LoginResponse.builder().token(jwtUtils.get(email, loginRequest.isRememberMe())).build();
    }

    public UserEntity getUserEntityByEmail(String email) {
        return userRepository.findById(email).stream().findFirst().orElseThrow(() -> new UserException.UserNotFoundException("User with email `" + email + "` not found"));
    }

    public UserResponse getMinimizedUserResponseByEmail(String email) {
        return userMapper.toMinimizedUserResponse(getUserEntityByEmail(email));
    }

    public UserResponse getUserResponseByToken() {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);
        jwtUtils.verify(token, true);

        UserEntity user = userRepository.findById(email).stream().findFirst().orElseThrow(() -> new UserException.UserNotFoundException("User with email `" + email + "` not found"));
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(SigninRequest signinRequest) {
        String email = signinRequest.getEmail();
        if (emailExists(email))
            throw new UserException.EmailAlreadyUsedException("Email `" + email + "` already used");

        UserEntity user = userMapper.toUserEntity(signinRequest, true);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(UserRequest userRequest) {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);

        if (!emailExists(email))
            throw new UserException.UserNotFoundException("User with email `" + userRequest.getEmail() + "` not found");

        UserEntity userEntity = getUserEntityByEmail(email);
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setPassword(userEntity.getPassword().equals(userRequest.getPassword()) ? userEntity.getPassword() : bCryptPasswordEncoder.encode(userRequest.getPassword()));
        userEntity.setContacts(userRequest.getContacts());
        userEntity.setBalance(BigDecimal.valueOf(userRequest.getBalance()));

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public UserResponse updateUser(UserEntity userEntity) {
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public void deleteUser() {
        String token = jwtUtils.getToken();
        String email = jwtUtils.getEmail(token, true);

        if (!emailExists(email))
            throw new UserException.UserNotFoundException("User with email `" + email + "` not found");

        userRepository.deleteById(email);
    }

    public UserResponse addContact(String email) {
        String token = jwtUtils.getToken();
        String userEmail = jwtUtils.getEmail(token, true);

        if (!emailExists(userEmail))
            throw new UserException.UserNotFoundException("User with email `" + userEmail + "` not found");

        UserEntity userEntity = getUserEntityByEmail(userEmail);

        if(userEntity.getContacts().contains(email))
            throw new UserException.ContactAlreadyExistsException("Contact with email `" + email + "` already exists");

        if(!emailExists(email))
            throw new UserException.UserNotFoundException("User with email `" + email + "` not found");

        userEntity.getContacts().add(email);

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public boolean emailExists(String email) {
        return userRepository.findById(email).stream().findFirst().isPresent();
    }
}
