package com.example.notecalculator.exception;

/**
 * Thrown when a calculation with a given ID does not exist in the database.
 * This results in a 404 NOT FOUND response.
 */
public class CalculationNotFoundException extends RuntimeException {

    public CalculationNotFoundException(String message) {
        super(message);
    }
}
