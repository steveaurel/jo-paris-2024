package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.client.VenueRestClient;
import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import com.infoevent.eventservice.entities.Venue;
import com.infoevent.eventservice.schedules.EventStatusUpdater;
import com.infoevent.eventservice.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

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
    private final EventStatusUpdater eventStatusUpdater;

    /**
     * Creates a new event.
     *
     * @param event The event to create, expected to be valid.
     * @return ResponseEntity containing the created event.
     */
    @PostMapping("")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        log.info("API call to create event: {}", "");

        LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());

        LocalDateTime oneHourFromNow = LocalDateTime.now(ZoneId.systemDefault()).plusHours(1);

        if (eventDateTime.isBefore(oneHourFromNow)) {
            return ResponseEntity.badRequest().body("L'événement doit être programmé au moins une heure après l'heure actuelle.");
        }

        Venue venue = venueRestClient.getVenueById(event.getVenueID());
        if (venue == null) {
            return ResponseEntity.badRequest().body("Emplacement spécifié introuvable.");
        }

        event.setSeatAvailable(venue.getCapacity());

        event.setEventStatus(EventStatus.ACTIVE);

        Event createdEvent = eventService.createEvent(event, event.getOfferTypes());


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
    @GetMapping("")
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

    /**
     * Updates the available seats for an event.
     *
     * @param id The ID of the event to update.
     * @param seats The number of available seats to set.
     * @return ResponseEntity containing the updated event or an appropriate error message.
     */
    @PatchMapping("/{id}/update-seats")
    public ResponseEntity<?> updateEventSeats(@PathVariable Long id, @RequestParam int seats) {
        log.info("API call to update available seats for event with ID: {} to {}", id, seats);

        if (seats < 0) {
            return ResponseEntity.badRequest().body("The number of seats purchased cannot be negative.");
        }

        Optional<Event> existingEvent = eventService.findEventById(id);
        if (existingEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event eventToUpdate = existingEvent.get();

        eventToUpdate.setSeatAvailable(eventToUpdate.getSeatAvailable() - seats);
        Event updatedEvent = eventService.updateEvent(id, eventToUpdate);

        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Event>> findAllActiveEvents() {
        log.info("API call to fetch all active events");
        List<Event> activeEvents = eventService.findAllActiveEvents();
        for (Event event : activeEvents) {
            try {
                Venue venue = venueRestClient.getVenueById(event.getVenueID());
                event.setVenue(venue);
            } catch (Exception e) {
                log.error("Failed to retrieve venue details for event ID: {}", event.getId(), e);
            }
        }
        return ResponseEntity.ok(activeEvents);
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<Boolean> validateEventStatus(@PathVariable Long id) {
        log.info("API call to validate event status for ID: {}", id);
        Optional<Event> eventOptional = eventService.findEventById(id);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOptional.get();
        eventStatusUpdater.updateEventStatus(event);
        boolean isValid = event.getEventStatus() == EventStatus.ACTIVE;

        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkEventAvailability(
            @RequestParam Long venueID,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime,
            @RequestParam LocalDate date) {
        log.info("Checking availability for venueId: {}, startTime: {}, endTime: {}, date: {}", venueID, startTime, endTime, date);
        boolean isAvailable = eventService.checkEventAvailability(venueID, startTime, endTime, date);
        return ResponseEntity.ok(isAvailable);
    }


}
