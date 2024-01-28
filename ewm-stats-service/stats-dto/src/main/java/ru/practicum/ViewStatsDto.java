package ru.practicum;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class ViewStatsDto {
    private String app;

    private String uri;

    private Integer hits;
}
