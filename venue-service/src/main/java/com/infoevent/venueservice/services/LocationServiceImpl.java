package com.infoevent.venueservice.services;

import com.infoevent.venueservice.entities.Location;
import com.infoevent.venueservice.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;

    @Override
    @Transactional()
    public Location createLocation(Location location) {
        log.info("Creating a new location in {}, {}", location.getCity(), location.getCountry());
        return locationRepository.save(location);
    }

    @Override
    @Transactional()
    public Location updateLocation(Long id, Location location) {
        log.info("Updating location with id: {}", id);
        return locationRepository.findById(id)
                .map(existingLocation -> {
                    existingLocation.setAddress(location.getAddress());
                    existingLocation.setCity(location.getCity());
                    existingLocation.setCountry(location.getCountry());
                    return locationRepository.save(existingLocation);
                }).orElseThrow(() -> new RuntimeException("Location not found with id " + id));
    }
    @Override
    @Transactional()
    public void deleteLocation(Long id) {
        log.info("Deleting location with id: {}", id);
        locationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        log.info("Fetching all locations");
        return locationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> getLocationById(Long id) {
        log.info("Fetching location by id: {}", id);
        return locationRepository.findById(id);
    }
}
