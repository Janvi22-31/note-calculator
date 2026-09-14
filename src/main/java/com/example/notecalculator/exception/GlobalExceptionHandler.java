package com.example.notecalculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception handling for the whole application.
 * Instead of writing try/catch in every controller method, Spring
 * automatically routes exceptions here, and we turn them into
 * clean JSON error responses with the correct HTTP status code.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Triggered when @Valid fails on a request body (e.g. missing/negative amount)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Invalid input");

        ErrorResponse error = new ErrorResponse("Invalid amount", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Triggered when the denomination is not one of the allowed values
    @ExceptionHandler(InvalidDenominationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDenomination(InvalidDenominationException ex) {
        ErrorResponse error = new ErrorResponse("Invalid denomination", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Triggered when a calculation is looked up by an ID that doesn't exist
    @ExceptionHandler(CalculationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CalculationNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Catch-all for anything unexpected (e.g. database connection issues)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
        ErrorResponse error = new ErrorResponse("Internal server error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
