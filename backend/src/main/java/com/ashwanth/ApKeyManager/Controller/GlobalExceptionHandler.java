package com.ashwanth.ApKeyManager.Controller;

import com.ashwanth.ApKeyManager.DTO.ErrorResponse;
import com.ashwanth.ApKeyManager.Exception.LimitReachedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LimitReachedException.class)
    public ResponseEntity<ErrorResponse> handleLimitReachedException(LimitReachedException ex) {
        return new ResponseEntity<>(
            new ErrorResponse("limit_reached", ex.getMessage()),
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        // Return a generic internal error message to avoid leaking details
        return new ResponseEntity<>(
            new ErrorResponse("internal_error", "Something went wrong. Please try again."),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}