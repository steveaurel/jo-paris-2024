package com.infoevent.venueservice.services;

import com.infoevent.venueservice.entities.Location;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the Location service.
 * Defines operations for managing locations.
 */
public interface LocationService {

    /**
     * Create a new location.
     *
     * @param location Location to be created
     * @return the saved location
     */
    public Location createLocation(Location location);

    /**
     * Update an existing location.
     *
     * @param id the id of the location to update
     * @param location Location information to update
     * @return the updated location
     */
    public Location updateLocation(Long id, Location location);

    /**
     * Delete a location by its id.
     *
     * @param id the id of the location to delete
     */
    public void deleteLocation(Long id);

    /**
     * Get all locations.
     *
     * @return a list of all locations
     */
    public List<Location> getAllLocations();

    /**
     * Get a location by its id.
     *
     * @param id the id of the location to fetch
     * @return an Optional containing the location if found, or an empty Optional if not found
     */
    public Optional<Location> getLocationById(Long id);
}
