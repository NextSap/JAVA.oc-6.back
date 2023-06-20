package com.paymybuddy.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.paymybuddy.backend.exception.JWTException;
import com.paymybuddy.backend.exception.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class JWTUtils {
    private static final JWTUtils INSTANCE = new JWTUtils();
    private final Logger logger = LogManager.getLogger(JWTUtils.class);
    private final Algorithm algorithm;

    private JWTUtils() {
        algorithm = Algorithm.HMAC256("secret");
    }

    public String get(String email, boolean rememberMe) {
        try {
        logger.debug("Entering getJWTToken with email: {}", email);

        String jwtToken = JWT.create().withSubject(email).withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (rememberMe ? 864000000 : 7200000))).sign(algorithm);

        logger.debug("Returning JWT token: {}", jwtToken);
        return jwtToken;
        } catch (Exception e) {
            throw new JWTException.CreatingTokenException("Error creating JWT token");
        }
    }

    public String verify(String token) {
        try {
            logger.debug("Entering verifyJWTToken with token: {}", token);

            String email = JWT.require(algorithm).build().verify(token.replace("Bearer ", "")).getSubject();

            logger.debug("Returning email: {}", email);
            return email;
        } catch (Exception e) {
            throw new UserException.BadCredentialsException("Bad credentials");
        }
    }

    public static JWTUtils getInstance() {
        return INSTANCE;
    }
}
