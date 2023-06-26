package com.paymybuddy.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.paymybuddy.backend.exception.JwtException;
import com.paymybuddy.backend.exception.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

public class JwtUtils {
    private static final JwtUtils INSTANCE = new JwtUtils();
    private final Logger logger = LogManager.getLogger(JwtUtils.class);
    private final Algorithm algorithm;

    private JwtUtils() {
        algorithm = Algorithm.HMAC256("secret");
    } // TODO: Use Vault to store secret

    public String get(String email, boolean rememberMe) {
        try {
            logger.debug("Entering getJWTToken with email: {}", email);

            String jwtToken = JWT.create().withSubject(email).withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (rememberMe ? 864000000 : 7200000))).sign(algorithm);

            logger.debug("Returning JWT token: {}", jwtToken);
            return jwtToken;
        } catch (Exception e) {
            throw new JwtException.CreatingTokenException("Error creating JWT token");
        }
    }

    public String getEmail(String token, boolean throwException) {
        try {
            logger.debug("Entering verifyJWTToken with token: {}", token);

            String email = JWT.require(algorithm).build().verify(token.replace("Bearer ", "")).getSubject();

            logger.debug("Returning email: {}", email);
            return email;
        } catch (Exception e) {
            if (throwException) {
                logger.error("Invalid token: {}", token);
                throw new UserException.BadCredentialsException("Invalid token");
            } else {
                return null;
            }
        }
    }

    public boolean verify(String token, boolean throwException) {
        return getEmail(token, throwException) != null;
    }

    public String getToken() {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    }

    public static JwtUtils getInstance() {
        return INSTANCE;
    }
}
