package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.Venue;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "VENUE-SERVICE")
public interface VenueRestClient {
    @GetMapping("/venues/{id}")
    @CircuitBreaker(name = "venueservice", fallbackMethod = "getDefaultVenue")
    Venue getVenueById(@PathVariable Long id);

    default Venue getDefaultVenue(Long id, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue with ID: " + id + " could not be retrieved.", throwable);
    }
}
