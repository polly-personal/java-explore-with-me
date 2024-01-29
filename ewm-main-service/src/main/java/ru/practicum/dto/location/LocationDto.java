package ru.practicum.dto.location;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Builder
@Data
public class LocationDto {
//    @Max(100000)
//    @Min(0)
    @DecimalMax(value = "90.0000000")
    @DecimalMin(value = "-90.0000000")
    private float lat;

    @DecimalMax(value = "180.0000000")
    @DecimalMin(value = "-180.0000000")
    private float lon;
}
