package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Price;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the price management service.
 * Defines operations for managing prices within the system.
 */
public interface PriceService {

    /**
     * Creates a new price.
     *
     * @param price The price to be created.
     * @return The created price.
     */
    Price createPrice(Price price);

    /**
     * Finds a price by its ID.
     *
     * @param id The ID of the price.
     * @return An Optional containing the found price, if available.
     */
    Optional<Price> findPriceById(Long id);

    /**
     * Retrieves all prices.
     *
     * @return A list of all prices.
     */
    List<Price> findAllPrices();

    /**
     * Updates an existing price.
     *
     * @param id    The ID of the price to be updated.
     * @param price The updated price information.
     * @return The updated price.
     */
    Price updatePrice(Long id, Price price);

    /**
     * Deletes a price by its ID.
     *
     * @param id The ID of the price to be deleted.
     */
    void deletePrice(Long id);

}
