package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${ewm-app-name}")
    private String appName;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public EndpointHitDto create(HttpServletRequest request) {
        log.info("🟫🟧 POST /hit");
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        ResponseEntity<EndpointHitDto> response = post("hit", endpointHitDto, EndpointHitDto.builder().build());

        log.info("🟩 информация сохранена, создано попадание на сайт={} ", response.getBody());
        return response.getBody();
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, String[] uris,
                                      boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", uris,
                "unique", unique
        );
        log.info("🟫🟧 GET /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);

        ResponseEntity<Object> response = get("stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters, new ArrayList<>());

        log.info("🟦 выдан список попаданий на сайты={} ", response.getBody());
        return response;
    }
}
