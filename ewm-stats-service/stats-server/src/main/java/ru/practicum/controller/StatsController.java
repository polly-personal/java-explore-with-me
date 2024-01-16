package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.StatsExceptionIncorrectRequestParam;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.StatsConstantDateTimeFormat.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping
@RestController
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> create(@Validated @RequestBody EndpointHitDto endpointHitDto) {
        log.info("🟫 POST /hit");

        EndpointHitDto result = statsService.create(endpointHitDto);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.status(201)
                .headers(responseHeaders)
                .body(result);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> get(@RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                                  @RequestParam(required = false) String[] uris,
                                  @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("🟫 GET /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);

        if (end.isBefore(start)) {
            throw new StatsExceptionIncorrectRequestParam("ошибка параметра/ов запроса: время \"end\" не может " +
                    "начинаться раньше, чем время \"start\"");
        }

        return statsService.getByRequestParam(start, end, uris, unique);
    }
}
