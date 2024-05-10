package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.Event;
import com.infoevent.ticketservice.entities.OfferType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "EVENT-SERVICE")
public interface EventRestClient {

    @GetMapping("/events/{id}")
    @CircuitBreaker(name = "eventservice", fallbackMethod = "getDefaultEvent")
    Event getEventById(@PathVariable Long id);

    @PatchMapping("/events/{id}/update-seats")
    @CircuitBreaker(name = "eventservice", fallbackMethod = "updateDefaultEventSeat")
    Event updateEventSeat(@PathVariable Long id, @RequestParam int seats);

    @GetMapping("/offertypes/{id}")
    @CircuitBreaker(name = "eventservice", fallbackMethod = "getDefaultOfferType")
    OfferType getOfferTypeById(@PathVariable Long id);

    @PutMapping("/events/{id}")
    @CircuitBreaker(name = "eventservice", fallbackMethod = "updateDefaultEvent")
    Event updateEvent(@PathVariable Long id, @RequestBody Event event);

    default Event getDefaultEvent(Long id, Throwable throwable) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID: " + id + " could not be retrieved.", throwable);
    }

    default OfferType getDefaultOfferType(Long id, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OfferType with ID: " + id + " could not be retrieved.", throwable);
    }

    default Event updateDefaultEventSeat(Long id, int seats, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Event seats for ID: " + id + " could not be updated.", throwable);
    }

    default Event updateDefaultEvent(Long id, Event event, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Event seats for ID: " + id + " could not be updated.", throwable);
    }
}
