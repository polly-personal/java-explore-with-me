package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;
import ru.practicum.repository.ViewStatsProjection;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = StatsMapper.toEndpointHit(endpointHitDto);
        EndpointHit entity = statsRepository.save(endpointHit);

        EndpointHitDto result = StatsMapper.toEndpointHitDto(entity);
        return result;
    }

    public List<ViewStatsDto> getByRequestParam(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<ViewStatsProjection> repoResult;

        if (uris != null && unique) {
            repoResult = statsRepository.getEndpointsHitsByDatesAndUrisWhereUniqueIp(start, end, Arrays.asList(uris));
        } else if (uris != null && unique == false) {
            repoResult = statsRepository.getEndpointsHitsByDatesAndUris(start, end, Arrays.asList(uris));
        } else if (uris == null && unique) {
            repoResult = statsRepository.getEndpointsHitsByDatesWhereUniqueIp(start, end);
        } else {
            repoResult = statsRepository.getEndpointsHitsOnlyByDates(start, end);
        }

        List<ViewStatsDto> viewStatsDtos = repoResult.stream()
                .map(statistics -> {
                    ViewStatsDto viewStatsDto = ViewStatsDto.builder()
                            .app(statistics.getApp())
                            .uri(statistics.getUri())
                            .hits(statistics.getHits())
                            .build();
                    return viewStatsDto;
                })
                .collect(Collectors.toList());

        return viewStatsDtos;
    }
}
