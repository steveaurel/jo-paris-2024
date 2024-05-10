package com.infoevent.venueservice.controllers;

import com.infoevent.venueservice.entities.Venue;
import com.infoevent.venueservice.services.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling API requests for Venue entities.
 * Provides endpoints for creating, updating, deleting, and fetching venues.
 */
@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
@Slf4j
public class VenueController {

    private final VenueService venueService;

    @PostMapping("")
    public ResponseEntity<Venue> createVenue(@Valid @RequestBody Venue venue) {
        log.info("API call to create venue");
        Venue createdVenue = venueService.createVenue(venue);
        return ResponseEntity.ok(createdVenue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @Valid @RequestBody Venue venue) {
        log.info("API call to update venue with id: {}", id);
        Venue updatedVenue = venueService.updateVenue(id, venue);
        return ResponseEntity.ok(updatedVenue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        log.info("API call to delete venue with id: {}", id);
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<List<Venue>> findAllVenues() {
        log.info("API call to fetch all venues");
        List<Venue> venues = venueService.findAllVenues();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> findVenueById(@PathVariable Long id) {
        log.info("API call to fetch venue by id: {}", id);
        return venueService.findVenueById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
