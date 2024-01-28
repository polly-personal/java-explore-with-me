package ru.practicum.dto.event;

import lombok.*;

@ToString(callSuper = true)
@Getter
@Setter
public class UpdateEventAdminRequest extends UpdateEventRequest {

    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
