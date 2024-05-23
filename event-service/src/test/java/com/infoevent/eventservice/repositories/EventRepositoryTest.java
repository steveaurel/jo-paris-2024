package com.infoevent.eventservice.repositories;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveEvent() {
        Event event = Event.builder()
                .name("Event 1")
                .description("Description 1")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(1L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(100)
                .build();

        Event savedEvent = eventRepository.save(event);
        assertNotNull(savedEvent.getId());
        assertEquals("Event 1", savedEvent.getName());
    }

    @Test
    public void testFindById() {
        Event event = Event.builder()
                .name("Event 1")
                .description("Description 1")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(1L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(100)
                .build();

        Event savedEvent = eventRepository.save(event);
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());
        assertTrue(foundEvent.isPresent());
        assertEquals("Event 1", foundEvent.get().getName());
    }

    @Test
    public void testFindAll() {
        Event event1 = Event.builder()
                .name("Event 1")
                .description("Description 1")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(1L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(100)
                .build();

        Event event2 = Event.builder()
                .name("Event 2")
                .description("Description 2")
                .date(LocalDate.of(2024, 5, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .build();

        eventRepository.save(event1);
        eventRepository.save(event2);

        List<Event> events = eventRepository.findAll();
        assertEquals(2, events.size());
    }

    @Test
    public void testDeleteById() {
        Event event = Event.builder()
                .name("Event 1")
                .description("Description 1")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(1L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(100)
                .build();

        Event savedEvent = eventRepository.save(event);
        eventRepository.deleteById(savedEvent.getId());
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());
        assertFalse(foundEvent.isPresent());
    }
}
