package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.EndpointHitDto;
import ru.practicum.entity.EndpointHit;

@Slf4j
@UtilityClass
public class StatsMapper {

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();

        log.info("🔀 \nDTO: " + endpointHitDto + " сконвертирован в \nJPA-сущность: " + endpointHit);
        return endpointHit;
    }

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();

        log.info("🔀 \nJPA-сущность: " + endpointHit + " сконвертирована в \nDTO: " + endpointHitDto);
        return endpointHitDto;
    }
}
