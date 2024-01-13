package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/* черновик */
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@RestController
public class MainController {
    private final StatsClient statsClient;

    @GetMapping("/{id}")
    public EndpointHitDto get(@PathVariable @Min(1) long id) {
        log.info("🟫 GET /events/{}", id);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .timestamp("2022-09-06 11:00:23")
                .build();
        ResponseEntity<Object> response = statsClient.create(endpointHitDto);
        System.out.println(response);
        return null;
    }
}
