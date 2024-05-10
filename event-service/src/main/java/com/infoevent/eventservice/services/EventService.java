package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.OfferType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for event management service.
 * Defines the operations for managing events within the system.
 */
public interface EventService {

    /**
     * Creates a new event.
     *
     * @param event The event to create.
     * @return The created event.
     */
    Event createEvent(Event event, Set<OfferType> offerTypes);

    /**
     * Finds an event by its ID.
     *
     * @param id The ID of the event.
     * @return An Optional containing the found event, if any.
     */
    Optional<Event> findEventById(Long id);

    /**
     * Retrieves all events.
     *
     * @return A list of all events.
     */
    List<Event> findAllEvents();

    /**
     * Updates an existing event.
     *
     * @param id The ID of the event to update.
     * @param event The updated event information.
     * @return The updated event.
     */
    Event updateEvent(Long id, Event event);

    /**
     * Deletes an event by its ID.
     *
     * @param id The ID of the event to delete.
     */
    void deleteEvent(Long id);

    /**
     * Finds all events for a specific venue.
     *
     * @param venueID The ID of the venue.
     * @return A list of events associated with the venue.
     */
    List<Event> findEventsByVenueID(Long venueID);


    List<Event> findAllActiveEvents();

    boolean checkEventAvailability(Long venueID, LocalTime startTime, LocalTime endTime, LocalDate date);

}
