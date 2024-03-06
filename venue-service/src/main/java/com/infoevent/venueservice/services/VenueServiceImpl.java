package com.infoevent.venueservice.services;

import com.infoevent.venueservice.entities.Venue;
import com.infoevent.venueservice.repositories.VenueRepository;
import com.infoevent.venueservice.services.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the VenueService.
 * Handles business logic for managing venues.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public Venue createVenue(Venue venue) {
        log.info("Creating a new venue: {}", venue.getName());
        return venueRepository.save(venue);
    }

    @Override
    public Venue updateVenue(Long id, Venue venue) {
        log.info("Updating venue with id: {}", id);
        return venueRepository.findById(id)
                .map(existingVenue -> {
                    existingVenue.setName(venue.getName());
                    existingVenue.setCapacity(venue.getCapacity());
                    existingVenue.setLocation(venue.getLocation());
                    return venueRepository.save(existingVenue);
                }).orElseThrow(() -> new RuntimeException("Venue not found with id " + id));
    }

    @Override
    public void deleteVenue(Long id) {
        log.info("Deleting venue with id: {}", id);
        venueRepository.deleteById(id);
    }

    @Override
    public List<Venue> findAllVenues() {
        log.info("Fetching all venues");
        return venueRepository.findAll();
    }

    @Override
    public Optional<Venue> findVenueById(Long id) {
        log.info("Fetching venue by id: {}", id);
        return venueRepository.findById(id);
    }
}
