package com.infoevent.venueservice.controllers;

import com.infoevent.venueservice.services.LocationService;
import com.infoevent.venueservice.entities.Location;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing locations.
 */
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationsById(@PathVariable Long id) {
        log.info("API call to fetch location by id: {}", id);
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) {
        log.info("API call to create a location");
        Location createdLocation = locationService.createLocation(location);
        return ResponseEntity.ok(createdLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @Valid @RequestBody Location location) {
        log.info("API call to update location with id: {}", id);
        Location updatedLocation = locationService.updateLocation(id, location);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.info("API call to delete location with id: {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<List<Location>> getAllLocations() {
        log.info("API call to fetch all locations");
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

}

