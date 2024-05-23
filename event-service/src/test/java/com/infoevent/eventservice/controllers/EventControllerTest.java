package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.client.VenueRestClient;
import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import com.infoevent.eventservice.entities.Venue;
import com.infoevent.eventservice.schedules.EventStatusUpdater;
import com.infoevent.eventservice.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private VenueRestClient venueRestClient;

    @MockBean
    private EventStatusUpdater eventStatusUpdater;

    private Event event;
    private Venue venue;

    @BeforeEach
    public void setUp() {
        event = Event.builder()
                .id(1L)
                .name("Event 1")
                .description("Description 1")
                .date(LocalDate.of(2024, 5, 19))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .venueID(1L)
                .eventStatus(EventStatus.ACTIVE)
                .seatAvailable(100)
                .build();

        venue = Venue.builder()
                .id(1L)
                .name("Venue 1")
                .capacity(200)
                .build();
    }

    @Test
    public void testCreateEvent() throws Exception {
        when(venueRestClient.getVenueById(anyLong())).thenReturn(venue);
        when(eventService.createEvent(any(Event.class), any())).thenReturn(event);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Event 1\", \"description\": \"Description 1\", \"date\": \"2024-08-19\", \"startTime\": \"10:00:00\", \"endTime\": \"12:00:00\", \"venueID\": 1, \"offerTypes\": [{\"description\": \"Offer 1\", \"seatQuantity\": 100, \"price\": {\"amount\": 50.0, \"currency\": \"USD\"}}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event 1"));

        verify(eventService, times(1)).createEvent(any(Event.class), any());
    }

    @Test
    public void testCreateEventWithInvalidDate() throws Exception {
        LocalDateTime invalidDateTime = LocalDateTime.now().plusMinutes(30); // less than one hour from now

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Event 1\", \"description\": \"Description 1\", \"date\": \"" + invalidDateTime.toLocalDate() + "\", \"startTime\": \"" + invalidDateTime.toLocalTime() + "\", \"endTime\": \"12:00:00\", \"venueID\": 1, \"offerTypes\": [{\"description\": \"Offer 1\", \"seatQuantity\": 100, \"price\": {\"amount\": 50.0, \"currency\": \"USD\"}}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("L'événement doit être programmé au moins une heure après l'heure actuelle."));

        verify(eventService, never()).createEvent(any(Event.class), any());
    }

    @Test
    public void testCreateEventWithInvalidVenue() throws Exception {
        when(venueRestClient.getVenueById(anyLong())).thenReturn(null);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Event 1\", \"description\": \"Description 1\", \"date\": \"2024-08-19\", \"startTime\": \"10:00:00\", \"endTime\": \"12:00:00\", \"venueID\": 1, \"offerTypes\": [{\"description\": \"Offer 1\", \"seatQuantity\": 100, \"price\": {\"amount\": 50.0, \"currency\": \"USD\"}}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Emplacement spécifié introuvable."));

        verify(eventService, never()).createEvent(any(Event.class), any());
    }

    @Test
    public void testFindEventById() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.of(event));
        when(venueRestClient.getVenueById(anyLong())).thenReturn(venue);

        mockMvc.perform(get("/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event 1"));

        verify(eventService, times(1)).findEventById(1L);
    }

    @Test
    public void testFindEventByIdNotFound() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).findEventById(1L);
    }

    @Test
    public void testFindEventById_Exception() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.of(event));
        when(venueRestClient.getVenueById(anyLong())).thenThrow(new RuntimeException("Failed to retrieve venue"));

        mockMvc.perform(get("/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event 1"));

        verify(eventService, times(1)).findEventById(1L);
        verify(venueRestClient, times(1)).getVenueById(anyLong());
    }

    @Test
    public void testFindAllEvents() throws Exception {
        when(eventService.findAllEvents()).thenReturn(Arrays.asList(event));
        when(venueRestClient.getVenueById(anyLong())).thenReturn(venue);

        mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Event 1"));

        verify(eventService, times(1)).findAllEvents();
    }

    @Test
    public void testFindAllEvents_Exception() throws Exception {
        when(eventService.findAllEvents()).thenReturn(Arrays.asList(event));
        when(venueRestClient.getVenueById(anyLong())).thenThrow(new RuntimeException("Failed to retrieve venue"));

        mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Event 1"));

        verify(eventService, times(1)).findAllEvents();
        verify(venueRestClient, times(1)).getVenueById(anyLong());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        when(eventService.updateEvent(anyLong(), any(Event.class))).thenReturn(event);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Updated Event\", \"description\": \"Updated Description\", \"date\": \"2024-05-20\", \"startTime\": \"14:00:00\", \"endTime\": \"16:00:00\", \"venueID\": 2 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event 1"));

        verify(eventService, times(1)).updateEvent(anyLong(), any(Event.class));
    }

    @Test
    public void testDeleteEvent() throws Exception {
        doNothing().when(eventService).deleteEvent(anyLong());

        mockMvc.perform(delete("/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEvent(1L);
    }

    @Test
    public void testFindEventsByVenueID() throws Exception {
        when(eventService.findEventsByVenueID(anyLong())).thenReturn(Arrays.asList(event));

        mockMvc.perform(get("/events/by-venue/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Event 1"));

        verify(eventService, times(1)).findEventsByVenueID(1L);
    }

    @Test
    public void testUpdateEventSeats() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.of(event));
        when(eventService.updateEvent(anyLong(), any(Event.class))).thenReturn(event);

        mockMvc.perform(patch("/events/1/update-seats")
                        .param("seats", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatAvailable").value(90));

        verify(eventService, times(1)).findEventById(1L);
        verify(eventService, times(1)).updateEvent(anyLong(), any(Event.class));
    }

    @Test
    public void testUpdateEventSeatsNegativeSeats() throws Exception {
        mockMvc.perform(patch("/events/1/update-seats")
                        .param("seats", "-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The number of seats purchased cannot be negative."));

        verify(eventService, never()).updateEvent(anyLong(), any(Event.class));
    }

    @Test
    public void testUpdateEventSeatsEventNotFound() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(patch("/events/1/update-seats")
                        .param("seats", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).findEventById(1L);
        verify(eventService, never()).updateEvent(anyLong(), any(Event.class));
    }

    @Test
    public void testFindAllActiveEvents() throws Exception {
        when(eventService.findAllActiveEvents()).thenReturn(Arrays.asList(event));
        when(venueRestClient.getVenueById(anyLong())).thenReturn(venue);

        mockMvc.perform(get("/events/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Event 1"));

        verify(eventService, times(1)).findAllActiveEvents();
    }

    @Test
    public void testFindAllActiveEvents_Exception() throws Exception {
        when(eventService.findAllActiveEvents()).thenReturn(Arrays.asList(event));
        when(venueRestClient.getVenueById(anyLong())).thenThrow(new RuntimeException("Failed to retrieve venue"));

        mockMvc.perform(get("/events/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Event 1"));

        verify(eventService, times(1)).findAllActiveEvents();
        verify(venueRestClient, times(1)).getVenueById(anyLong());
    }

    @Test
    public void testValidateEventStatus() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.of(event));
        doNothing().when(eventStatusUpdater).updateEventStatus(any(Event.class));

        mockMvc.perform(get("/events/validate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(eventService, times(1)).findEventById(1L);
        verify(eventStatusUpdater, times(1)).updateEventStatus(any(Event.class));
    }

    @Test
    public void testValidateEventStatusNotFound() throws Exception {
        when(eventService.findEventById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/events/validate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).findEventById(1L);
        verify(eventStatusUpdater, never()).updateEventStatus(any(Event.class));
    }

    @Test
    public void testValidateEventStatusInactive() throws Exception {
        event.setEventStatus(EventStatus.FINISHED);
        when(eventService.findEventById(anyLong())).thenReturn(Optional.of(event));
        doNothing().when(eventStatusUpdater).updateEventStatus(any(Event.class));

        mockMvc.perform(get("/events/validate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        verify(eventService, times(1)).findEventById(1L);
        verify(eventStatusUpdater, times(1)).updateEventStatus(any(Event.class));
    }

    @Test
    public void testCheckEventAvailability() throws Exception {
        when(eventService.checkEventAvailability(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class)))
                .thenReturn(true);

        mockMvc.perform(get("/events/check-availability")
                        .param("venueID", "1")
                        .param("startTime", "10:00:00")
                        .param("endTime", "12:00:00")
                        .param("date", "2024-08-19")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(eventService, times(1)).checkEventAvailability(anyLong(), any(LocalTime.class), any(LocalTime.class), any(LocalDate.class));
    }
}
