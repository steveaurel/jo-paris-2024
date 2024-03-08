package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.client.VenueRestClient;
import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.entities.Venue;
import com.infoevent.eventservice.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * REST Controller for managing events.
 * Provides endpoints for creating, retrieving, updating, and deleting events,
 * as well as fetching events for a specific venue.
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final VenueRestClient venueRestClient;

    /**
     * Creates a new event.
     *
     * @param event The event to create, expected to be valid.
     * @return ResponseEntity containing the created event.
     */
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        log.info("API call to create event: {}", event.getName());
        Set<Price> prices = event.getPrices();
        Event createdEvent = eventService.createEvent(event, prices);
        return ResponseEntity.ok(createdEvent);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param id The ID of the event to retrieve.
     * @return ResponseEntity containing the requested event, if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> findEventById(@PathVariable Long id) {
        log.info("API call to find event by ID: {}", id);
        return eventService.findEventById(id)
                .map(event -> {
                    try {
                        Venue venue = venueRestClient.getVenueById(event.getVenueID());
                        event.setVenue(venue);
                    } catch (Exception e) {
                        log.error("Failed to retrieve venue details for event ID: {}", id, e);
                    }
                    return ResponseEntity.ok(event);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all events.
     *
     * @return ResponseEntity containing a list of all events.
     */
    @GetMapping
    public ResponseEntity<List<Event>> findAllEvents() {
        log.info("API call to list all events");
        List<Event> events = eventService.findAllEvents();
        events.forEach(event -> {
            try {
                Venue venue = venueRestClient.getVenueById(event.getVenueID());
                event.setVenue(venue);
            } catch (Exception e) {
                log.error("Failed to retrieve venue details for event ID: {}", event.getId(), e);
            }
        });
        return ResponseEntity.ok(events);
    }

    /**
     * Updates an existing event.
     *
     * @param id The ID of the event to update.
     * @param event The updated event information.
     * @return ResponseEntity containing the updated event.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        log.info("API call to update event with ID: {}", id);
        Event updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id The ID of the event to delete.
     * @return ResponseEntity indicating the operation result.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.info("API call to delete event with ID: {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves events by a specific venue ID.
     *
     * @param venueID The ID of the venue to find events for.
     * @return ResponseEntity containing a list of events held at the specified venue.
     */
    @GetMapping("/by-venue/{venueID}")
    public ResponseEntity<List<Event>> findEventsByVenueID(@PathVariable Long venueID) {
        log.info("API call to fetch events for venue ID: {}", venueID);
        List<Event> events = eventService.findEventsByVenueID(venueID);
        return ResponseEntity.ok(events);
    }
}
