package com.paymybuddy.backend.exception;

import com.paymybuddy.backend.object.response.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JWTException {

    private final Logger logger = LogManager.getLogger(JWTException.class);

    @ExceptionHandler(CreatingTokenException.class)
    public ResponseEntity<ErrorResponse> handleJWTTokenException(CreatingTokenException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().field("JWTToken").cause(exception.getMessage()).build();
        logger.error("JWT token creation failed: " + exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class CreatingTokenException extends RuntimeException {
        public CreatingTokenException(String message) {
            super(message);
        }
    }
}
