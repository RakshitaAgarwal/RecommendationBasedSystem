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

    public static class InvalidChoiceException extends Exception {
        public InvalidChoiceException(String message) {
            super(message);
        }
    }

    public static class BadResponseException extends Exception {
        public BadResponseException(String message) {
            super(message);
        }
    }

    public static class BadRequestException extends Exception {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class EmptyResponseException extends Exception {
        public EmptyResponseException(String message) {
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