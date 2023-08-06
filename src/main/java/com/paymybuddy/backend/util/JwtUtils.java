package com.paymybuddy.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.paymybuddy.backend.exception.JwtException;
import com.paymybuddy.backend.exception.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private final Logger logger = LogManager.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secret;

    private JwtUtils() {
    }

    public JwtUtils(String secret) {
        this.secret = secret;
    }

    public String get(String email, boolean rememberMe) {
        try {
            logger.debug("Entering getJWTToken with email: {}", email);

            String jwtToken = JWT.create().withSubject(email).withIssuedAt(new Date())
                    .withExpiresAt(Instant.ofEpochSecond(new Date().getTime() + (rememberMe ? 864000000 : 7200000))).sign(Algorithm.HMAC256(secret));

            logger.debug("Returning JWT token: {}", jwtToken);
            return jwtToken;
        } catch (Exception e) {
            throw new JwtException.CreatingTokenException("Error creating JWT token");
        }
    }

    public String getEmail(String token, boolean throwException) {
        System.out.println(token);
        try {
            logger.debug("Entering verifyJWTToken with token: {}", token);

            String email = JWT.require(Algorithm.HMAC256(secret)).build().verify(token.replace("Bearer ", "")).getSubject();

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
}
