package com.infoevent.eventservice.client;

import com.infoevent.eventservice.entities.Venue;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@FeignClient(name = "VENUE-SERVICE")
public interface VenueRestClient {
    @GetMapping("/venues/{id}")
    @CircuitBreaker(name = "venueservice", fallbackMethod = "getDefaultVenue")
    Venue getVenueById(@PathVariable Long id);

    @GetMapping("/venues")
    @CircuitBreaker(name = "venueservice", fallbackMethod = "getDefaultVenues")
    List<Venue> getAllVenues();

    @PostMapping("/venues")
    @CircuitBreaker(name = "venueservice", fallbackMethod = "createDefaultVenue")
    Venue createVenue(@Valid @RequestBody Venue venue);

    default Venue getDefaultVenue(Long id, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue with ID: " + id + " could not be retrieved.", throwable);
    }

    default List<Venue> getDefaultVenues(Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venues could not be retrieved.", throwable);
    }

    default Venue createDefaultVenue(Venue venue, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Venue could not be created due to service unavailability.", throwable);
    }
}
