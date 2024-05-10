package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.client.VenueRestClient;
import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.Venue;
import com.infoevent.eventservice.services.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventControllerTest {
    @Mock
    private EventService eventService;

    @Mock
    private VenueRestClient venueRestClient;

    @InjectMocks
    private EventController eventController;


    @Test
    public void testCreateEventSuccessfully() {
        Event mockEvent = new Event();
        Venue mockVenue = new Venue();
        mockEvent.setDate(LocalDate.now().plusDays(1));
        mockEvent.setStartTime(LocalTime.of(15, 0));
        mockEvent.setEndTime(LocalTime.of(17, 0));
        mockVenue.setCapacity(500);
        mockEvent.setVenueID(1L);
        when(venueRestClient.getVenueById(anyLong())).thenReturn(mockVenue);
        when(eventService.createEvent(any(Event.class), anySet())).thenReturn(mockEvent);

        ResponseEntity<?> response = eventController.createEvent(mockEvent);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testFindEventByIdWithSuccess() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        Venue mockVenue = new Venue();
        when(eventService.findEventById(1L)).thenReturn(Optional.of(mockEvent));
        when(venueRestClient.getVenueById(anyLong())).thenReturn(mockVenue);

        ResponseEntity<Event> response = eventController.findEventById(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testFindAllEventsSuccess() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventService.findAllEvents()).thenReturn(events);
        when(venueRestClient.getVenueById(anyLong())).thenReturn(new Venue());

        ResponseEntity<List<Event>> response = eventController.findAllEvents();
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testUpdateEventSuccess() {
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        Event updatedEvent = new Event();
        when(eventService.updateEvent(eq(1L), any(Event.class))).thenReturn(updatedEvent);

        ResponseEntity<Event> response = eventController.updateEvent(1L, new Event());
        assertEquals(200, response.getStatusCodeValue());
        assertSame(updatedEvent, response.getBody());
    }
}
