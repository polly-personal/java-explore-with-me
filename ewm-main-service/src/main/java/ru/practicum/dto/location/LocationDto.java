package ru.practicum.dto.location;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Builder
@Data
public class LocationDto {
    @Max(100000)
    @Min(0)
    private float lat;

    @Max(100000)
    @Min(0)
    private float lon;
}
