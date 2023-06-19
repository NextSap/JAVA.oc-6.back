package com.paymybuddy.backend.exception;

import com.paymybuddy.backend.object.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JWTException {
    @ExceptionHandler(CreatingTokenException.class)
    public ResponseEntity<ErrorResponse> handleJWTTokenException(CreatingTokenException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().field("JWTToken").cause(exception.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VerifyingTokenException.class)
    public ResponseEntity<ErrorResponse> handleJWTTokenException(VerifyingTokenException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().field("JWTToken").cause(exception.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    public static class CreatingTokenException extends RuntimeException {
        public CreatingTokenException(String message) {
            super(message);
        }
    }

    public static class VerifyingTokenException extends RuntimeException {
        public VerifyingTokenException(String message) {
            super(message);
        }
    }
}
