package ru.practicum;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EndpointHitDto {
    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
