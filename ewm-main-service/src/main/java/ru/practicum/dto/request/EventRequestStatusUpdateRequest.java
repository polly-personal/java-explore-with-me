package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.dto.PatchValidation;
import ru.practicum.entity.request.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Jacksonized
@Builder
@Data
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "поле \"requestIds\" должно быть заполнено", groups = PatchValidation.class)
    private List<Long> requestIds;

    @NotNull(message = "поле \"status\" должно быть заполнено", groups = PatchValidation.class)
    private RequestStatus status;
}
