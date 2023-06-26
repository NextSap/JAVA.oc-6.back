package com.paymybuddy.backend.exception;

import com.paymybuddy.backend.object.response.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationException {

    private final Logger logger = LogManager.getLogger(ValidationException.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = ex.getAllErrors()
                .stream().map(error -> {
                    String[] splitViolation = Objects.requireNonNull(error.getDefaultMessage()).split(":");
                    return ErrorResponse.builder().field(splitViolation[0]).cause(splitViolation[1]).build();
                }).collect(Collectors.toList());

        logger.error("ValidationException: " + Arrays.toString(errors.toArray()));

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<ErrorResponse>> getErrorsMap(List<ErrorResponse> errors) {
        Map<String, List<ErrorResponse>> errorResponse = new HashMap<>();
        errorResponse.put("validationErrors", errors);
        return errorResponse;
    }
}
