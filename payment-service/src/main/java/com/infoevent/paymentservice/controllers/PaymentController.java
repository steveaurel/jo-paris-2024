package com.infoevent.paymentservice.controllers;

import com.infoevent.paymentservice.entities.Payment;
import com.infoevent.paymentservice.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing payments.
 * Provides endpoints for creating and retrieving payments,
 * as well as fetching payments for a specific event and user.
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates a new payment.
     *
     * @param payment The event to create, expected to be valid.
     * @return ResponseEntity containing the created event.
     */
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        log.info("API call to create new payment for user ID: {} and event ID: {}", payment.getUserID(), payment.getEventID());
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id The ID of the payment to retrieve.
     * @return ResponseEntity containing the requested payment, if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> findPaymentById(@PathVariable Long id) {
        log.info("API call to find payment by ID: {}", id);
        return paymentService.findPaymentById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all payments.
     *
     * @return ResponseEntity containing a list of all payments.
     */
    @GetMapping
    public ResponseEntity<List<Payment>> findAllPayments() {
        log.info("API call to list all payments");
        List<Payment> payments = paymentService.findAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Retrieves payments by a specific event ID.
     *
     * @param eventID The ID of the event to find payments for.
     * @return ResponseEntity containing a list of payments held at the specified event.
     */
    @GetMapping("/by-event/{eventID}")
    public ResponseEntity<List<Payment>> findPaymentsByEventID(@PathVariable Long eventID) {
        log.info("API call to fetch payments for event ID: {}", eventID);
        List<Payment> payments = paymentService.findPaymentsByEventID(eventID);
        return ResponseEntity.ok(payments);
    }

    /**
     * Retrieves payments by a specific user ID.
     *
     * @param userID The ID of the user to find payments for.
     * @return ResponseEntity containing a list of payments held at the specified user.
     */
    @GetMapping("/by-user/{userID}")
    public ResponseEntity<List<Payment>> findPaymentsByUserID(@PathVariable Long userID) {
        log.info("API call to fetch payments for user ID: {}", userID);
        List<Payment> payments = paymentService.findPaymentsByUserID(userID);
        return ResponseEntity.ok(payments);
    }
}
