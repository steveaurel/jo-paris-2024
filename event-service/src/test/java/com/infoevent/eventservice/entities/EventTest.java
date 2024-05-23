package com.infoevent.eventservice.entities;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void testEventGettersAndSetters() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Event Name");
        event.setDescription("Event Description");
        event.setDate(LocalDate.of(2024, 5, 19));
        event.setStartTime(LocalTime.of(10, 0));
        event.setEndTime(LocalTime.of(12, 0));
        event.setVenueID(100L);
        event.setEventStatus(EventStatus.ACTIVE);
        event.setSeatAvailable(50);
        event.setOfferTypes(new HashSet<>());

        assertEquals(1L, event.getId());
        assertEquals("Event Name", event.getName());
        assertEquals("Event Description", event.getDescription());
        assertEquals(LocalDate.of(2024, 5, 19), event.getDate());
        assertEquals(LocalTime.of(10, 0), event.getStartTime());
        assertEquals(LocalTime.of(12, 0), event.getEndTime());
        assertEquals(100L, event.getVenueID());
        assertEquals(EventStatus.ACTIVE, event.getEventStatus());
        assertEquals(50, event.getSeatAvailable());
        assertNotNull(event.getOfferTypes());
    }

    @Test
    public void testEventConstructor() {
        Event event = new Event(1L, "Event Name", "Event Description", LocalDate.of(2024, 5, 19),
                LocalTime.of(10, 0), LocalTime.of(12, 0), new HashSet<>(), 100L, null,
                EventStatus.ACTIVE, 50);

        assertEquals(1L, event.getId());
        assertEquals("Event Name", event.getName());
        assertEquals("Event Description", event.getDescription());
        assertEquals(LocalDate.of(2024, 5, 19), event.getDate());
        assertEquals(LocalTime.of(10, 0), event.getStartTime());
        assertEquals(LocalTime.of(12, 0), event.getEndTime());
        assertEquals(100L, event.getVenueID());
        assertEquals(EventStatus.ACTIVE, event.getEventStatus());
        assertEquals(50, event.getSeatAvailable());
        assertNotNull(event.getOfferTypes());
    }

    @Test
    public void testEventBuilder() {
        Venue venue = new Venue(); // Assuming you have a Venue class

        Event event = Event.builder()
                .id(1L)
                .name("Event Name")
                .description("Event Description")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(100L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(50)
                .offerTypes(new HashSet<>())
                .venue(venue)
                .build();

        assertEquals(venue, event.getVenue());
    }

    @Test
    public void testEventToString() {
        Event event = new Event(1L, "Event Name", "Event Description", LocalDate.of(2024, 5, 19),
                LocalTime.of(10, 0), LocalTime.of(12, 0), new HashSet<>(), 100L, null,
                EventStatus.ACTIVE, 50);

        String expected = "Event(id=1, name=Event Name, description=Event Description, date=2024-05-19, startTime=10:00, endTime=12:00, offerTypes=[], venueID=100, eventStatus=ACTIVE, seatAvailable=50)";
        assertEquals(expected, event.toString());
    }

    @Test
    public void testEventBuilderToString() {
        Event.EventBuilder builder = Event.builder()
                .id(1L)
                .name("Event Name")
                .description("Event Description")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(100L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(50)
                .offerTypes(new HashSet<>());

        String builderToString = builder.toString();
        assertTrue(builderToString.contains("Event.EventBuilder"));
        assertTrue(builderToString.contains("id=1"));
        assertTrue(builderToString.contains("name=Event Name"));
        assertTrue(builderToString.contains("description=Event Description"));
        assertTrue(builderToString.contains("date=2024-05-19"));
        assertTrue(builderToString.contains("startTime=10:00"));
        assertTrue(builderToString.contains("endTime=12:00"));
        assertTrue(builderToString.contains("venueID=100"));
        assertTrue(builderToString.contains("eventStatus=ACTIVE"));
        assertTrue(builderToString.contains("seatAvailable=50"));
        assertTrue(builderToString.contains("offerTypes=[]"));
    }
}
