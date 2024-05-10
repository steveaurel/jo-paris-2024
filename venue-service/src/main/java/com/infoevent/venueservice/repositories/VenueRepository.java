package com.infoevent.venueservice.repositories;

import com.infoevent.venueservice.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Venue entity.
 * Extends JpaRepository to provide CRUD operations for Venue entities.
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findAllByOrderByNameAsc();
}
