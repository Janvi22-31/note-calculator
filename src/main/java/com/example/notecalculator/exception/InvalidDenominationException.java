package com.example.notecalculator.exception;

/**
 * Thrown when the user selects a denomination that is not one of the
 * allowed currency notes (10, 50, 100, 200, 500, 2000).
 */
public class InvalidDenominationException extends RuntimeException {

    public InvalidDenominationException(String message) {
        super(message);
    }
}
