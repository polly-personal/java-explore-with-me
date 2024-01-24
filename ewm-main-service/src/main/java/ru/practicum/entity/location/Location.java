package ru.practicum.entity.location;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "locations")
@Entity
public class Location {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private float lat;

    private float lon;
}
