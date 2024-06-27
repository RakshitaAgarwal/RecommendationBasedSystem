package org.cafeteria.common.customException;

public class CustomExceptions extends Exception {
    public static class InvalidRequestException extends Exception {
        public InvalidRequestException(String message) {
            super(message);
        }
    }

    public static class InvalidResponseException extends Exception {
        public InvalidResponseException(String message) {
            super(message);
        }
    }

    public static class LoginFailedException extends Exception {
        public LoginFailedException(String message) {
            super(message);
        }
    }

    public static class DuplicateEntryFoundException extends Exception {
        public DuplicateEntryFoundException(String message) {
            super(message);
        }
    }
}