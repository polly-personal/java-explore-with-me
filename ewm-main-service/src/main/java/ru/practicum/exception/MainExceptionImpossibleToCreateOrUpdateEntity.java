package ru.practicum.exception;

public class MainExceptionImpossibleToCreateOrUpdateEntity extends RuntimeException {
    public MainExceptionImpossibleToCreateOrUpdateEntity(String message) {
        super(message);
    }
}
