package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    /* наследники от Runtime */
    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, MainExceptionIncorrectDateTime.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handelRuntimeException(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("400 BAD_REQUEST")
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 запрос составлен некорректно: " + apiError.toString());
        return apiError;
    }

    /* наследники от Throwable */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handelMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("400 BAD_REQUEST")
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 запрос составлен некорректно (некорректное значение переменной пути/параметра запроса): " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler(MainExceptionIdNotFound.class)
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

    @ExceptionHandler(MainExceptionImpossibleToPublicGetEntity.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handelMainExceptionImpossibleToPublicGetEntity(MainExceptionImpossibleToPublicGetEntity e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("404 NOT_FOUND")
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 объект/сущность/dto не найден или не доступен: " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler(MainExceptionIncompatibleIds.class)
    @ResponseStatus(HttpStatus.CONFLICT)
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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handelConstraintViolationException(DataIntegrityViolationException e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("409 CONFLICT")
                .reason("Integrity constraint has been violated. OR For the requested operation the conditions are not met.")
                .message(e.getMessage() + "; SQL [n/a]; ")
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 нарушение целостности данных в бд: " + apiError.toString());
        return apiError;
    }

    @ExceptionHandler(MainExceptionImpossibleToCreateOrUpdateEntity.class)
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

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handelThrowable(Throwable e) {
        ApiError apiError = ApiError.builder()
                .errors(List.of(e.getStackTrace()).subList(0, 1))
                .status("500 INTERNAL_SERVER_ERROR")
                .reason("Server error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn("🟥📱 ошибка сервера: " + apiError.toString());
        return apiError;
    }
}
