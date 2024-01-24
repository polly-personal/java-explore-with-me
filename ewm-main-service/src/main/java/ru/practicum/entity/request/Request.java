package ru.practicum.entity.request;

import lombok.*;
import ru.practicum.entity.event.Event;
import ru.practicum.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "requests")
@Entity
public class Request {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime created;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;

    @JoinColumn(name = "requester_id")
    @ManyToOne
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
