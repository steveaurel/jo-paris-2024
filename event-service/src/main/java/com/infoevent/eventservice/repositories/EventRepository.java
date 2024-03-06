package com.infoevent.eventservice.repositories;

import com.infoevent.eventservice.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for {@link Event} entities.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Finds all events by venue ID.
     *
     * @param venueID The ID of the venue.
     * @return A list of events associated with the given venue ID.
     */
    List<Event> findByVenueID(Long venueID);
}
