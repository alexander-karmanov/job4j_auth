package ru.job4j.exception;

public class InvalidPersonCredentialsException extends IllegalArgumentException {
    public InvalidPersonCredentialsException(String message) {
        super(message);
    }
}
