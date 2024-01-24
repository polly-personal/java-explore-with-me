package ru.practicum.service.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.entity.location.Location;
import ru.practicum.repository.location.LocationRepository;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public Location getByLatAndLonOrCreateEntity(Location location) {
        Location repoResult = locationRepository.findByLatAndLon(location.getLat(), location.getLon()).orElse(null);

        if (repoResult == null) {
            Location createdLocation = locationRepository.save(location);

            log.info("🟩 создана локация: " + createdLocation);
            return createdLocation;
        }

        log.info("🟦 выдана локация: " + repoResult);
        return repoResult;
    }
}
