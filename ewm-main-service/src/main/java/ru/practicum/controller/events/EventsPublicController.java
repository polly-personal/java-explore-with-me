package ru.practicum.controller.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.MainExceptionIncorrectDateTime;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class EventsPublicController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping("{eventId}")
    public EventFullDto getForPublicUsersById(@PathVariable long eventId, HttpServletRequest request) {
        log.info("🟫🟫 GET /events/{}", eventId);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.create(endpointHitDto);

        return eventService.getForPublicUsersById(eventId);
    }

    @GetMapping
    public List<EventShortDto> getAllForPublicUsers(@RequestParam(defaultValue = "") String text,
                                                    @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                                    @RequestParam(defaultValue = "false") Boolean paid,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                                    @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(defaultValue = "VIEWS") String sort,
                                                    @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                                    HttpServletRequest request) {
        log.info("🟫🟫 GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort" +
                "={}&from={}&size={}", text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new MainExceptionIncorrectDateTime("ошибка параметра/ов запроса: время \"rangeEnd\" не может начинаться раньше, чем время \"rangeStart\"");
        }

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.create(endpointHitDto);

        return eventService.getAllForPublicUsers(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
