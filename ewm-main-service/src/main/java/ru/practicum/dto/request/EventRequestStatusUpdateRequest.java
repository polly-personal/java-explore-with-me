package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Jacksonized
@Builder
@Data
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "поле \"requestIds\" должно быть заполнено")
    private Set<Long> requestIds;

    @NotNull(message = "поле \"status\" должно быть заполнено")
    private Status status;

    public enum Status {
        CONFIRMED,
        REJECTED,
    }
}
