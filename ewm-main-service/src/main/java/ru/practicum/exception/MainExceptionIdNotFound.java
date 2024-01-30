package ru.practicum.exception;

public class MainExceptionIdNotFound extends RuntimeException {
    public MainExceptionIdNotFound(String message) {
        super(message);
    }
}
