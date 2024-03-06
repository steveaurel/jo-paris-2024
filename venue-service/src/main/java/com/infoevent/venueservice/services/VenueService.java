package com.infoevent.venueservice.services;

import com.infoevent.venueservice.entities.Venue;
import java.util.List;
import java.util.Optional;

/**
 * Interface for the Venue service.
 * Defines operations for managing venues.
 */
public interface VenueService {

    /**
     * Creates a new venue.
     *
     * @param venue the venue to create
     * @return the created venue
     */
    Venue createVenue(Venue venue);

    /**
     * Updates an existing venue.
     *
     * @param id the id of the venue to update
     * @param venue the updated venue information
     * @return the updated venue
     */
    Venue updateVenue(Long id, Venue venue);

    /**
     * Deletes a venue by its id.
     *
     * @param id the id of the venue to delete
     */
    void deleteVenue(Long id);

    /**
     * Retrieves all venues.
     *
     * @return a list of all venues
     */
    List<Venue> findAllVenues();

    /**
     * Retrieves a venue by its id.
     *
     * @param id the id of the venue to find
     * @return an Optional containing the found venue, or an empty Optional if no venue is found
     */
    Optional<Venue> findVenueById(Long id);
}
