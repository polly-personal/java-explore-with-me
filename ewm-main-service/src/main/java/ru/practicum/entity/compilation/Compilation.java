package ru.practicum.entity.compilation;

import lombok.*;
import ru.practicum.entity.event.Event;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "compilations")
@Entity
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "is_pinned")
    private Boolean pinned;

    @ToString.Exclude
    @JoinTable(name = "compilations_events", joinColumns = @JoinColumn(name = "compilation_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ManyToMany
    private Set<Event> events;
}