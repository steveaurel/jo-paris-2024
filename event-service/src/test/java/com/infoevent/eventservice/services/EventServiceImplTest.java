package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private Set<OfferType> offerTypes;

    @BeforeEach
    public void setUp() {
        event = Event.builder()
                .name("Event 1")
                .description("Description 1")
                .date(LocalDate.of(2024, 8, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(1L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(100)
                .build();

        OfferType offerType = new OfferType();
        offerType.setDescription("Offer 1");
        offerType.setSeatQuantity(100);
        offerType.setPrice(new Price());

        offerTypes = new HashSet<>();
        offerTypes.add(offerType);
    }

    @Test
    public void testCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event, offerTypes);
        assertNotNull(createdEvent);
        assertEquals("Event 1", createdEvent.getName());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testCreateEventWithNullOfferTypes() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(event, null));
        assertEquals("Offer types must not be null or empty", thrown.getMessage());
    }

    @Test
    public void testCreateEventWithEmptyOfferTypes() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(event, Collections.emptySet()));
        assertEquals("Offer types must not be null or empty", thrown.getMessage());
    }

    @Test
    public void testCreateEventWithOfferTypeWithoutPrice() {
        OfferType offerTypeWithoutPrice = new OfferType();
        offerTypeWithoutPrice.setDescription("Offer 1");
        offerTypeWithoutPrice.setSeatQuantity(100);
        Set<OfferType> offerTypesWithoutPrice = new HashSet<>();
        offerTypesWithoutPrice.add(offerTypeWithoutPrice);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(event, offerTypesWithoutPrice));
        assertEquals("Price information must not be null for offer type", thrown.getMessage());
    }

    @Test
    public void testFindEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> foundEvent = eventService.findEventById(1L);
        assertTrue(foundEvent.isPresent());
        assertEquals("Event 1", foundEvent.get().getName());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAllEvents() {
        Event event2 = Event.builder()
                .name("Event 2")
                .description("Description 2")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .build();

        when(eventRepository.findAllByOrderByDateAscStartTimeAsc()).thenReturn(Arrays.asList(event, event2));

        List<Event> events = eventService.findAllEvents();
        assertEquals(2, events.size());
        verify(eventRepository, times(1)).findAllByOrderByDateAscStartTimeAsc();
    }

    @Test
    public void testUpdateEvent() {
        Event updatedEvent = Event.builder()
                .name("Updated Event")
                .description("Updated Description")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .offerTypes(event.getOfferTypes())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(1L, updatedEvent);
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEventNotFound() {
        Event updatedEvent = Event.builder()
                .id(1L)
                .name("Updated Event")
                .description("Updated Description")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .offerTypes(event.getOfferTypes())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> eventService.updateEvent(1L, updatedEvent));
        assertEquals("Event not found with ID: 1", thrown.getMessage());
    }

    @Test
    public void testUpdateEventWithNonNullOfferTypes() {
        Event updatedEvent = Event.builder()
                .id(1L)
                .name("Updated Event")
                .description("Updated Description")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .offerTypes(offerTypes)
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(1L, updatedEvent);
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        assertNotNull(result.getOfferTypes());
        assertEquals(offerTypes.size(), result.getOfferTypes().size());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEventWithNullOfferTypes() {
        Event updatedEvent = Event.builder()
                .name("Updated Event")
                .description("Updated Description")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .offerTypes(null)
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(1L, updatedEvent);
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEventWithNullExistingOfferTypes() {
        event.setOfferTypes(null);
        Event updatedEvent = Event.builder()
                .id(1L)
                .name("Updated Event")
                .description("Updated Description")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .offerTypes(new HashSet<>())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(1L, updatedEvent);
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        assertNotNull(result.getOfferTypes());
        assertTrue(result.getOfferTypes().isEmpty());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEventWithNonNullExistingOfferTypesAndNonNullUpdatedOfferTypes() {
        Event updatedEvent = Event.builder()
                .id(1L)
                .name("Updated Event")
                .description("Updated Description")
                .date(LocalDate.of(2024, 8, 20))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .venueID(2L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(200)
                .offerTypes(offerTypes)
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(1L, updatedEvent);
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        assertNotNull(result.getOfferTypes());
        assertEquals(offerTypes.size(), result.getOfferTypes().size());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testDeleteEvent() {
        doNothing().when(eventRepository).deleteById(1L);

        eventService.deleteEvent(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindEventsByVenueID() {
        when(eventRepository.findByVenueID(1L)).thenReturn(Arrays.asList(event));

        List<Event> events = eventService.findEventsByVenueID(1L);
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findByVenueID(1L);
    }

    @Test
    public void testFindAllActiveEvents() {
        when(eventRepository.findAllByEventStatusOrderByDateAscStartTimeAsc(EventStatus.ACTIVE)).thenReturn(Arrays.asList(event));

        List<Event> events = eventService.findAllActiveEvents();
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findAllByEventStatusOrderByDateAscStartTimeAsc(EventStatus.ACTIVE);
    }

    @Test
    public void testCheckEventAvailability() {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        LocalDate date = LocalDate.of(2024, 8, 19);

        when(eventRepository.findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        when(eventRepository.findEventsByVenueIDAndStartTimeBetweenAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        boolean isAvailable = eventService.checkEventAvailability(1L, startTime, endTime, date);
        assertTrue(isAvailable);

        verify(eventRepository, times(1)).findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
        verify(eventRepository, times(1)).findEventsByVenueIDAndStartTimeBetweenAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
    }

    @Test
    public void testCheckEventAvailabilityWhenEventsOverlap() {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        LocalDate date = LocalDate.of(2024, 8, 19);

        when(eventRepository.findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(event));
        when(eventRepository.findEventsByVenueIDAndStartTimeBetweenAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(event));

        boolean isAvailable = eventService.checkEventAvailability(1L, startTime, endTime, date);
        assertFalse(isAvailable);

        verify(eventRepository, times(1)).findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
        verify(eventRepository, times(1)).findEventsByVenueIDAndStartTimeBetweenAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
    }

    @Test
    public void testCheckEventAvailabilityWhenEventsStartingDuringEvent() {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        LocalDate date = LocalDate.of(2024, 8, 19);

        when(eventRepository.findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        when(eventRepository.findEventsByVenueIDAndStartTimeBetweenAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(event));

        boolean isAvailable = eventService.checkEventAvailability(1L, startTime, endTime, date);
        assertFalse(isAvailable);

        verify(eventRepository, times(1)).findEventsByVenueIDAndEndTimeGreaterThanAndEndTimeLessThanAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
        verify(eventRepository, times(1)).findEventsByVenueIDAndStartTimeBetweenAndDate(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
    }

}
