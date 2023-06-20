package com.paymybuddy.backend.exception;

import com.paymybuddy.backend.object.response.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionException {

    private final Logger logger = LogManager.getLogger(TransactionException.class);

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(TransactionNotFoundException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().field("Transaction").cause(exception.getMessage()).build();
        logger.error("Transaction not found: " + exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionInvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleException(TransactionInvalidAmountException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().field("Transaction").cause(exception.getMessage()).build();
        logger.error("Transaction invalid amount: " + exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionInvalidTypeException.class)
    public ResponseEntity<ErrorResponse> handleException(TransactionInvalidTypeException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().field("Transaction").cause(exception.getMessage()).build();
        logger.error("Transaction invalid type: " + exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public static class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(String message) {
            super(message);
        }
    }

    public static class TransactionInvalidAmountException extends RuntimeException {
        public TransactionInvalidAmountException(String message) {
            super(message);
        }
    }

    public static class TransactionInvalidTypeException extends RuntimeException {
        public TransactionInvalidTypeException(String message) {
            super(message);
        }
    }
}
