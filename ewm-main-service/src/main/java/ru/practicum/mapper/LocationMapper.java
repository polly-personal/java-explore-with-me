package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.entity.location.Location;

@Slf4j
@UtilityClass
public class LocationMapper {
    public Location toLocation(LocationDto locationDto) {
        Location location = Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();

        log.info("🔀\nDTO: " + locationDto + " сконвертирован в \nJPA-сущность: " + location);
        return location;
    }

    public LocationDto toLocationDto(Location location) {
        LocationDto locationDto = LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();

        log.info("🔀\nDTO: " + location + " сконвертирован в \nJPA-сущность: " + locationDto);
        return locationDto;
    }
}
