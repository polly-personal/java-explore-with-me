package ru.practicum.dto.location;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Builder
@Data
public class LocationDto {

    @DecimalMax(value = "90.0000000")
    @DecimalMin(value = "-90.0000000")
    private float lat;

    @DecimalMax(value = "180.0000000")
    @DecimalMin(value = "-180.0000000")
    private float lon;
}
