package ru.practicum.entity.comment;

import lombok.*;
import ru.practicum.entity.event.Event;
import ru.practicum.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "comments")
@Entity
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "commentator_id")
    @ManyToOne
    private User commentator;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;

    private String text;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
