package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.Event;
import com.infoevent.ticketservice.entities.OfferType;
import com.infoevent.ticketservice.entities.Venue;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "EVENT-SERVICE")
public interface EventRestClient {

    @GetMapping("/events/{id}")
    @CircuitBreaker(name = "eventservice", fallbackMethod = "getDefaultEvent")
    Event getEventById(@PathVariable Long id);

    @PatchMapping("/{id}/update-seats")
    @CircuitBreaker(name = "eventservice")
    Event updateEventSeat(@PathVariable Long id, @RequestParam int seats);
    @GetMapping("/offertypes/{id}")
    @CircuitBreaker(name = "eventservice", fallbackMethod = "getDefaultOfferType")
    OfferType getOfferTypeById(@PathVariable Long id);

    default Event getDefaultEvent(Long id, Throwable throwable) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID: " + id + " could not be retrieved.", throwable);
    }

    default Venue getDefaultOfferType(Long id, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OfferType with ID: " + id + " could not be retrieved.", throwable);
    }
}
