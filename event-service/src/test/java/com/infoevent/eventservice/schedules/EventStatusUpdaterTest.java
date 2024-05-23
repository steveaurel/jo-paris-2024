package com.infoevent.eventservice.schedules;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import com.infoevent.eventservice.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventStatusUpdaterTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventStatusUpdater eventStatusUpdater;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    private Event activeEvent;
    private Event inProgressEvent;
    private Event finishedEvent;
    private Event suspendedEvent;
    private Event cancelledEvent;

    @BeforeEach
    public void setUp() {
        activeEvent = Event.builder()
                .id(1L)
                .name("Active Event")
                .description("Description 1")
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .eventStatus(EventStatus.ACTIVE)
                .build();

        inProgressEvent = Event.builder()
                .id(2L)
                .name("In Progress Event")
                .description("Description 2")
                .date(LocalDate.now())
                .startTime(LocalTime.now().minusMinutes(15))
                .endTime(LocalTime.now().plusHours(1))
                .eventStatus(EventStatus.ACTIVE)
                .build();

        finishedEvent = Event.builder()
                .id(3L)
                .name("Finished Event")
                .description("Description 3")
                .date(LocalDate.now().minusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .eventStatus(EventStatus.ACTIVE)
                .build();

        suspendedEvent = Event.builder()
                .id(4L)
                .name("Suspended Event")
                .description("Description 4")
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .eventStatus(EventStatus.SUSPENDED)
                .build();

        cancelledEvent = Event.builder()
                .id(5L)
                .name("Cancelled Event")
                .description("Description 5")
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .eventStatus(EventStatus.CANCELLED)
                .build();
    }

    @Test
    public void testUpdateEventStatuses() {
        List<Event> events = Arrays.asList(activeEvent, inProgressEvent, finishedEvent);
        when(eventRepository.findAllByEventStatusNotIn(Arrays.asList(EventStatus.SUSPENDED, EventStatus.CANCELLED))).thenReturn(events);

        eventStatusUpdater.updateEventStatuses();

        verify(eventRepository, times(3)).save(eventCaptor.capture());
        List<Event> updatedEvents = eventCaptor.getAllValues();

        assertEquals(EventStatus.ACTIVE, updatedEvents.get(0).getEventStatus());
        assertEquals(EventStatus.IN_PROGRESS, updatedEvents.get(1).getEventStatus());
        assertEquals(EventStatus.FINISHED, updatedEvents.get(2).getEventStatus());
    }

    @Test
    public void testUpdateEventStatus() {
        eventStatusUpdater.updateEventStatus(activeEvent);
        assertEquals(EventStatus.ACTIVE, activeEvent.getEventStatus());

        eventStatusUpdater.updateEventStatus(inProgressEvent);
        assertEquals(EventStatus.IN_PROGRESS, inProgressEvent.getEventStatus());

        eventStatusUpdater.updateEventStatus(finishedEvent);
        assertEquals(EventStatus.FINISHED, finishedEvent.getEventStatus());
    }

    @Test
    public void testDoNotUpdateSuspendedOrCancelledEvents() {
        List<Event> events = Arrays.asList(suspendedEvent, cancelledEvent);
        when(eventRepository.findAllByEventStatusNotIn(Arrays.asList(EventStatus.SUSPENDED, EventStatus.CANCELLED))).thenReturn(Arrays.asList(activeEvent, inProgressEvent, finishedEvent));

        eventStatusUpdater.updateEventStatuses();

        verify(eventRepository, never()).save(suspendedEvent);
        verify(eventRepository, never()).save(cancelledEvent);
    }
}
