package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the {@link EventService}.
 * Provides functionality to manage events, including creation, retrieval, update, and deletion of events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Event createEvent(Event event, Set<OfferType> offerTypes) {
        log.info("Creating new event: {}", event.getName());
        if (offerTypes == null || offerTypes.isEmpty()) {
            throw new IllegalArgumentException("Offer types must not be null or empty");
        }
        event.setOfferTypes(offerTypes);
        offerTypes.forEach(offerType -> {
            if (offerType.getPrice() == null) {
                throw new IllegalArgumentException("Price information must not be null for offer type");
            }
            offerType.setEvent(event);
        });
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findEventById(Long id) {
        log.info("Finding event by ID: {}", id);
        return eventRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAllEvents() {
        log.info("Retrieving all events");
        return eventRepository.findAllByOrderByDateAscStartTimeAsc();
    }

    @Override
    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {
        log.info("Updating event with ID: {}", id);
        return eventRepository.findById(id).map(existingEvent -> {
            existingEvent.setName(updatedEvent.getName());
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setStartTime(updatedEvent.getStartTime());
            existingEvent.setEndTime(updatedEvent.getEndTime());
            existingEvent.setEventStatus(updatedEvent.getEventStatus());
            existingEvent.setVenueID(updatedEvent.getVenueID());

            if (updatedEvent.getOfferTypes() != null) {
                if (existingEvent.getOfferTypes() == null) {
                    existingEvent.setOfferTypes(new HashSet<>());
                } else {
                    existingEvent.getOfferTypes().clear();
                }
                existingEvent.getOfferTypes().addAll(updatedEvent.getOfferTypes());
            }

            return eventRepository.save(existingEvent);
        }).orElseThrow(() -> new IllegalStateException("Event not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deleting event with ID: {}", id);
        eventRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findEventsByVenueID(Long venueID) {
        log.info("Fetching events for venue ID: {}", venueID);
        return eventRepository.findByVenueID(venueID);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAllActiveEvents() {
        log.info("Retrieving all active events");
        return eventRepository.findAllByEventStatusOrderByDateAscStartTimeAsc(EventStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEventAvailability(Long venueID, LocalTime startTime, LocalTime endTime, LocalDate date) {
        log.info("Checking availability for venue ID: {}", venueID);
        LocalTime bufferStartTime = startTime.minusHours(2);
        List<Event> eventsEndingBeforeStart = eventRepository.findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(
                venueID, bufferStartTime, startTime, date);
        List<Event> eventsStartingDuringEvent = eventRepository.findEventsByVenueIDAndStartTimeBetweenAndDate(
                venueID, bufferStartTime, endTime, date);
        return eventsEndingBeforeStart.isEmpty() && eventsStartingDuringEvent.isEmpty();
    }
}