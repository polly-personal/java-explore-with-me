package ru.practicum.dto.event;

import lombok.*;

@ToString(callSuper = true)
@Getter
@Setter
public class UpdateEventUserRequest extends UpdateEventRequest {

    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }
}
