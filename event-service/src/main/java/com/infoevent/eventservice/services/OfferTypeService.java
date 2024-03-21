package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.OfferType;

import java.util.List;
import java.util.Optional;

public interface OfferTypeService {

    /**
     * Creates and saves a new offer type.
     *
     * @param offerType The offer type to be created.
     * @return The saved offer type entity.
     */
    OfferType createOfferType(OfferType offerType);

    /**
     * Finds an offer type by its ID.
     *
     * @param id The ID of the offer type to find.
     * @return An Optional containing the found offer type, if present.
     */
    Optional<OfferType> findOfferTypeById(Long id);

    /**
     * Retrieves all offer types from the database.
     *
     * @return A list of all saved offer types.
     */
    List<OfferType> findAllOfferTypes();

    /**
     * Updates an existing offer type identified by its ID with new information.
     *
     * @param id        The ID of the offer type to update.
     * @param offerType The new offer type information to apply.
     * @return The updated offer type entity.
     */
    OfferType updateOfferType(Long id, OfferType offerType);

    /**
     * Deletes an offer type by its ID.
     *
     * @param id The ID of the offer type to be deleted.
     */
    void deleteOfferType(Long id);
}
