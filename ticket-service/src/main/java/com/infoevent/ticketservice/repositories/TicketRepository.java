package com.infoevent.ticketservice.repositories;

import com.infoevent.ticketservice.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


/**
 * JPA repository for {@link Ticket} entities.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Finds all tickets by event ID.
     *
     * @param eventID The ID of the event.
     * @return A list of tickets associated with the given event ID.
     */
    List<Ticket> findByEventID(Long eventID);

    /**
     * Finds all tickets by user ID.
     *
     * @param userID The ID of the user.
     * @return A list of tickets associated with the given user ID.
     */
    List<Ticket> findByUserID(Long userID);

    Optional<Ticket> findByQrCode(byte [] qrCode);
}
