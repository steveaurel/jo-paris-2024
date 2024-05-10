package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.services.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
@Slf4j
public class PriceController {

    private final PriceService priceService;

    @PostMapping("")
    public ResponseEntity<Price> createPrice(@Valid @RequestBody Price price) {
        log.info("API call to create price");
        Price createdPrice = priceService.createPrice(price);
        return ResponseEntity.ok(createdPrice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Price> findPriceById(@PathVariable Long id) {
        log.info("API call to find price by ID: {}", id);
        return priceService.findPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<List<Price>> findAllPrices() {
        log.info("API call to list all prices");
        List<Price> prices = priceService.findAllPrices();
        return ResponseEntity.ok(prices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable Long id, @Valid @RequestBody Price price) {
        log.info("API call to update price with ID: {}", id);
        Price updatedPrice = priceService.updatePrice(id, price);
        return ResponseEntity.ok(updatedPrice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        log.info("API call to delete price with ID: {}", id);
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
    /*
    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<List<Price>> findPricesByEventId(@PathVariable Long eventId) {
        log.info("API call to fetch prices for event ID: {}", eventId);
        List<Price> prices = priceService.findPricesByEventId(eventId);
        return ResponseEntity.ok(prices);
    }*/
}
