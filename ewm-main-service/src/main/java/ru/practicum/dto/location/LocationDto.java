package ru.practicum.dto.location;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDto {
    private float lat;

    private float lon;
}
