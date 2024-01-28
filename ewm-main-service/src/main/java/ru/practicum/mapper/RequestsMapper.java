package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.request.Request;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class RequestsMapper {
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();

        log.info("🔀 \nJPA-сущность={} сконвертирована в \nDTO={}", request, participationRequestDto);
        return participationRequestDto;
    }

    public List<ParticipationRequestDto> toParticipationRequestDtos(List<Request> requests) {
        List<ParticipationRequestDto> participationRequestDtos = requests.stream()
                .map(RequestsMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей={} сконвертирован в \nсписок DTO={}", requests, participationRequestDtos);
        return participationRequestDtos;
    }
}
