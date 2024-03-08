package com.infoevent.eventservice.repositories;

import com.infoevent.eventservice.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for {@link Price} entities.
 */
@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    /**
     * Finds all prices associated with a given event ID.
     *
     * @param eventId The ID of the event.
     * @return A list of prices for the event.
     */
    List<Price> findByEventId(Long eventId);
}
