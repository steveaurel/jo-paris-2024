package com.infoevent.venueservice.repositories;

import com.infoevent.venueservice.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Location} entity.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
