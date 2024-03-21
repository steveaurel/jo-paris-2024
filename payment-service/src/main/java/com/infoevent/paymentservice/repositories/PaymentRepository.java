package com.infoevent.paymentservice.repositories;

import com.infoevent.paymentservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for {@link Payment} entities.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds all payments by event ID.
     *
     * @param eventID The ID of the event.
     * @return A list of payments associated with the given event ID.
     */
    List<Payment> findByEventID(Long eventID);

    /**
     * Finds all payments by user ID.
     *
     * @param userID The ID of the user.
     * @return A list of payments associated with the given user ID.
     */
    List<Payment> findByUserID(Long userID);
}
