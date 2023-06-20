package com.paymybuddy.backend.service;

import com.paymybuddy.backend.exception.UserException;
import com.paymybuddy.backend.mapper.UserMapper;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.AuthResponse;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.repository.UserRepository;
import com.paymybuddy.backend.util.CredentialUtils;
import com.paymybuddy.backend.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils = JWTUtils.getInstance();
    private final CredentialUtils credentialUtils = CredentialUtils.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse login(String email, String password, boolean rememberMe) {
       UserEntity user = userRepository.findById(email).stream().findFirst().orElseThrow(() -> new UserException.BadCredentialsException("Bad credentials"));

       if(!credentialUtils.check(password, user.getPassword()))
           throw new UserException.BadCredentialsException("Bad credentials");

        return AuthResponse.builder().token(jwtUtils.get(email, rememberMe)).build();
    }

    private UserEntity getUser(String email) {
        return userRepository.findById(email).stream().findFirst().orElseThrow(() -> new UserException.UserNotFoundException("User with email `"+email+"` not found"));
    }

    public UserResponse getUser(String email, String token) {
        jwtUtils.verify(token);

        UserEntity user = userRepository.findById(email).stream().findFirst().orElseThrow(() -> new UserException.UserNotFoundException("User with email `"+email+"` not found"));
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserRequest userRequest) {
        if(emailExists(userRequest.getEmail()))
            throw new UserException.EmailAlreadyUsedException("Email `"+userRequest.getEmail()+"` already used");

        UserEntity user = userMapper.toUserEntity(userRequest, true);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(UserRequest userRequest, String token) {
        String email = jwtUtils.verify(token);

        if(!emailExists(email))
            throw new UserException.UserNotFoundException("User with email `"+userRequest.getEmail()+"` not found");

        UserEntity userEntity = getUser(email);
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setPassword(userEntity.getPassword().equals(userRequest.getPassword()) ? userEntity.getPassword() : credentialUtils.hash(userRequest.getPassword()));
        userEntity.setContacts(userRequest.getContacts());
        userEntity.setBalance(userRequest.getBalance());

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public void deleteUser(String token) {
        String email = jwtUtils.verify(token);

        if(!emailExists(email))
            throw new UserException.UserNotFoundException("User with email `"+email+"` not found");

        userRepository.deleteById(email);
    }

    public boolean emailExists(String email) {
        return userRepository.findById(email).stream().findFirst().isPresent();
    }
}
