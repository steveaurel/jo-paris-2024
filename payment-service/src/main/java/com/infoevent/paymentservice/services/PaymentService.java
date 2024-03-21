package com.infoevent.paymentservice.services;

import com.infoevent.paymentservice.entities.Payment;

import java.util.List;
import java.util.Optional;

/**
 * Interface for payment management service.
 * Defines the operations for managing payments within the system.
 */
public interface PaymentService {

    /**
     * Creates a new payment.
     *
     * @param payment The payment to create.
     * @return The created payment.
     */
    Payment createPayment(Payment payment);

    /**
     * Finds a payment by its ID.
     *
     * @param id The ID of the payment.
     * @return An Optional containing the found payment, if any.
     */
    Optional<Payment> findPaymentById(Long id);

    /**
     * Retrieves all payments.
     *
     * @return A list of all payments.
     */
    List<Payment> findAllPayments();

    /**
     * Finds all payments for a specific event.
     *
     * @param eventID The ID of the event.
     * @return A list of payments associated with the event.
     */
    List<Payment> findPaymentsByEventID(Long eventID);

    /**
     * Finds all payments for a specific user.
     *
     * @param userID The ID of the user.
     * @return A list of payments associated with the user.
     */
    List<Payment> findPaymentsByUserID(Long userID);
}
