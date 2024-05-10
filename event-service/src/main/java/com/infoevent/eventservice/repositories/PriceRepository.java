package com.infoevent.eventservice.repositories;

import com.infoevent.eventservice.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * JPA repository for {@link Price} entities.
 */
@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

}
