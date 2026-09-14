package com.example.notecalculator.exception;

/**
 * Simple structure used to send consistent, friendly JSON error
 * messages to the frontend, e.g.:
 * {
 *   "error": "Invalid amount",
 *   "message": "Amount must be greater than zero"
 * }
 */
public class ErrorResponse {

    private String error;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
