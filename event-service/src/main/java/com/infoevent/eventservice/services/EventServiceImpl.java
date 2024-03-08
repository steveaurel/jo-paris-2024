package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.repositories.EventRepository;
import com.infoevent.eventservice.repositories.PriceRepository;
import com.infoevent.eventservice.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final PriceRepository priceRepository;

    @Override
    public Event createEvent(Event event, Set<Price> prices) {
        log.info("Creating new event: {}", event.getName());

        prices.forEach(event::addPrice);
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> findEventById(Long id) {
        log.info("Finding event by ID: {}", id);
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> findAllEvents() {
        log.info("Retrieving all events");
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        log.info("Updating event with ID: {}", id);
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setName(event.getName());
                    existingEvent.setDescription(event.getDescription());
                    existingEvent.setDate(event.getDate());
                    existingEvent.setTime(event.getTime());
                    existingEvent.setDuration(event.getDuration());
                    existingEvent.setPrices(event.getPrices());
                    existingEvent.setVenueID(event.getVenueID());
                    return eventRepository.save(existingEvent);
                }).orElseThrow(() -> new IllegalStateException("Event not found with ID: " + id));
    }

    @Override
    public void deleteEvent(Long id) {
        log.info("Deleting event with ID: {}", id);
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> findEventsByVenueID(Long venueID) {
        log.info("Fetching events for venue ID: {}", venueID);
        return eventRepository.findByVenueID(venueID);
    }

}
