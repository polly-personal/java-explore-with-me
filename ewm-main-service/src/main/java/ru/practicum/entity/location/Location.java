package ru.practicum.entity.location;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Embeddable
public class Location {

    private float lat;

    private float lon;
}
