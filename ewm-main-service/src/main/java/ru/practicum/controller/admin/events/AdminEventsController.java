package ru.practicum.controller.admin.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.PostValidation;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.event.EventService;

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
                                               @RequestBody @Validated(PostValidation.class) UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("🟫🟫 PATCH /admin/events/{}", eventId);
        return eventService.updateForAdminByEventId(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public List<EventFullDto> getAllForAdmin(@RequestParam(name = "users", required = false) List<Long> usersIds,
                                                         @RequestParam(required = false) List<String> states,
                                                         @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                                         @RequestParam(required = false, defaultValue = "0") int from,
                                                         @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("🟫🟫 GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}",
                usersIds, states, categoryIds, rangeStart, rangeEnd);
        return eventService.getAllForAdmin(usersIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }
}
