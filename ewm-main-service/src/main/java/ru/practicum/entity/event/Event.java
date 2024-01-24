package ru.practicum.entity.event;

import lombok.*;
import ru.practicum.entity.category.Category;
import ru.practicum.entity.location.Location;
import ru.practicum.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "events")
@Entity
public class Event {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String annotation;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @JoinColumn(name = "location_id")
    @ManyToOne
    private Location location;

    @Column(name = "is_paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "is_request_moderation")
    private Boolean requestModeration;

    private String title;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @JoinColumn(name = "initiator_id")
    @ManyToOne
    private User initiator;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private EventState state;

    private int views;
}
