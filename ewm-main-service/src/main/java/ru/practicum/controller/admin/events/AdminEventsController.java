package ru.practicum.controller.admin.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@RestController
public class AdminEventsController {
    private final EventService eventService;

    @PatchMapping("{eventId}")
    public EventFullDto patchForAdminByEventId(@PathVariable long eventId,
                                               @RequestBody @Validated UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("🟫🟫 PATCH /admin/events/{}", eventId);
        log.info("🟤 пришедшие параметры: eventId={}, updateEventAdminRequest={}", eventId, updateEventAdminRequest);
        return eventService.updateForAdminByEventId(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public List<EventFullDto> getAllForAdmin(@RequestParam(name = "users", required = false) List<Long> usersIds,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("🟫🟫 GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                usersIds, states, categoryIds, rangeStart, rangeEnd, from, size);
        return eventService.getAllForAdmin(usersIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }
}
