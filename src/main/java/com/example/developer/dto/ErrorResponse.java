package com.example.developer.dto;
import java.time.LocalDateTime;

public class ErrorResponse {
    private int status;
        private String message;
        private LocalDateTime timestamp;
    private String error;

    public ErrorResponse(String s) {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ErrorResponse() {
            timestamp = LocalDateTime.now();
        }

        public ErrorResponse(int status, String message) {
            this();
            this.status = status;
            this.message = message;
        }

        // Getters and Setters

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
}
