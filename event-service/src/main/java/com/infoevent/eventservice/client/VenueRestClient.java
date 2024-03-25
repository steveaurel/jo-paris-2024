package com.infoevent.eventservice.client;

import com.infoevent.eventservice.entities.Venue;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "VENUE-SERVICE")
public interface VenueRestClient {
    @GetMapping("/venues/{id}")
    @CircuitBreaker(name = "venueservice", fallbackMethod = "getDefaultVenue")
    Venue getVenueById(@PathVariable Long id);

    @GetMapping("/venues/")
    @CircuitBreaker(name = "venueservice", fallbackMethod = "getDefaultVenues")
    List<Venue> getAllVenues();

    default Venue getDefaultVenue(Long id, Exception e){
        return Venue.builder()
                .id(id)
                .name("source not available")
                .build();
    }

    default List<Venue> getDefaultVenues(Exception e){
        return List.of();
    }
}
