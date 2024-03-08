package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public Price createPrice(Price price) {
        log.info("Creating new price: {}", price.getAmount());
        return priceRepository.save(price);
    }

    @Override
    public Optional<Price> findPriceById(Long id) {
        log.info("Finding price by ID: {}", id);
        return priceRepository.findById(id);
    }

    @Override
    public List<Price> findAllPrices() {
        log.info("Retrieving all prices");
        return priceRepository.findAll();
    }

    @Override
    public Price updatePrice(Long id, Price price) {
        log.info("Updating price with ID: {}", id);
        return priceRepository.findById(id)
                .map(existingPrice -> {
                    existingPrice.setAmount(price.getAmount());
                    existingPrice.setOfferType(price.getOfferType());
                    existingPrice.setEvent(price.getEvent());
                    return priceRepository.save(existingPrice);
                }).orElseThrow(() -> new IllegalStateException("Price not found with ID: " + id));
    }

    @Override
    public void deletePrice(Long id) {
        log.info("Deleting price with ID: {}", id);
        priceRepository.deleteById(id);
    }

    @Override
    public List<Price> findPricesByEventId(Long eventId) {
        log.info("Fetching prices for event ID: {}", eventId);
        return priceRepository.findByEventId(eventId);
    }
}
