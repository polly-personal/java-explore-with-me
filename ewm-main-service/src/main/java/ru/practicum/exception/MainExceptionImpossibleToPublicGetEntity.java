package ru.practicum.exception;

public class MainExceptionImpossibleToPublicGetEntity extends RuntimeException {
    public MainExceptionImpossibleToPublicGetEntity(String message) {
        super(message);
    }
}
