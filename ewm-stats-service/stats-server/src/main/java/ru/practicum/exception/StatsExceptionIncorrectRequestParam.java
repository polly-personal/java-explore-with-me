package ru.practicum.exception;

public class StatsExceptionIncorrectRequestParam extends RuntimeException {
    public StatsExceptionIncorrectRequestParam(String message) {
        super(message);
    }
}
