package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.entity.request.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ParticipationRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private RequestStatus status;
}
