package ru.job4j.exception;

public class InvalidPersonIdException extends RuntimeException {
    public InvalidPersonIdException(String message) {
        super(message);
    }
}
