package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    @Transactional
    public Price createPrice(Price price) {
        log.info("Creating new price: {}", price.getAmount());
        return priceRepository.save(price);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Price> findPriceById(Long id) {
        log.info("Finding price by ID: {}", id);
        return priceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Price> findAllPrices() {
        log.info("Retrieving all prices");
        return priceRepository.findAll();
    }

    @Override
    @Transactional
    public Price updatePrice(Long id, Price updatedPrice) {
        log.info("Updating price with ID: {}", id);
        return priceRepository.findById(id)
                .map(existingPrice -> {
                    existingPrice.setAmount(updatedPrice.getAmount());
                    existingPrice.setOfferType(updatedPrice.getOfferType());
                    return priceRepository.save(existingPrice);
                }).orElseThrow(() -> new IllegalStateException("Price not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deletePrice(Long id) {
        log.info("Deleting price with ID: {}", id);
        priceRepository.deleteById(id);
    }
    /*
    @Override
    @Transactional(readOnly = true)
    public List<Price> findPricesByEventId(Long eventId) {
        log.info("Fetching prices for event ID: {}", eventId);
        return priceRepository.findByEventId(eventId);
    }
    */
}
