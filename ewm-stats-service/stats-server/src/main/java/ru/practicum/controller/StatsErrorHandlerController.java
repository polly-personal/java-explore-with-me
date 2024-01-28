package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.StatsExceptionIncorrectRequestParam;

@Slf4j
@RestControllerAdvice
public class StatsErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("🟥📊 400 - Bad Request={} ", e.getMessage(), e);
        return "🟥📊 некорректный json: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("🟥📊 400 - Bad Request={} ", e.getMessage(), e);
        return "🟥📊 пропущен обязательный параметр запроса: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleStatsExceptionIncorrectRequestParam(StatsExceptionIncorrectRequestParam e) {
        log.warn("🟥📊 400 - Bad Request={} ", e.getMessage(), e);
        return e.getMessage();
    }
}
