package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.exception.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("400 BAD_REQUEST")
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 запрос составлен некорректно (некорректный json): " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handelMethodArgumentNotValidException(MethodArgumentTypeMismatchException e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("400 BAD_REQUEST")
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 запрос составлен некорректно (некорректное значение переменной пути): " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handelMainExceptionInitiatorIdNotLinkedToEventId(MainExceptionIncompatibleIds e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("400 BAD_REQUEST")
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 запрос составлен некорректно (некорректное значение переменной пути): " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handelMethodArgumentNotValidException(MainExceptionIdNotFound e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("404 NOT_FOUND")
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 id не найден: " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handelConstraintViolationException(ConstraintViolationException e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("409 CONFLICT")
                .reason("Integrity constraint has been violated. OR For the requested operation the conditions are not met.")
                .message(e.getMessage() + "; SQL [n/a]; " + e.getSQLException())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 нарушение целостности данных в бд: " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handelMainExceptionIncorrectDateTime(MainExceptionIncorrectDateTime e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("409 CONFLICT")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 время и дата не удовлетворяет правилам создания: " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handelMainExceptionImpossibleToCreateEntity(MainExceptionImpossibleToCreateOrUpdateEntity e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("409 CONFLICT")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 сущность не удовлетворяет правилам редактирования: " + apiError.toString());
        return apiError;
    }
}
