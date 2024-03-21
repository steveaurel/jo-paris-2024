package com.infoevent.paymentservice.services;

import com.infoevent.paymentservice.entities.Payment;
import com.infoevent.paymentservice.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    @Override
    public Payment createPayment(Payment payment) {
        log.info("Creating new payment for user ID: {} and event ID: {}", payment.getUserID(), payment.getEventID());
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findPaymentById(Long id) {
        log.info("Finding payment by ID: {}", id);
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findAllPayments() {
        log.info("Retrieving all payments");
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> findPaymentsByEventID(Long eventID) {
        log.info("Fetching payments for event ID: {}", eventID);
        return paymentRepository.findByEventID(eventID);
    }

    @Override
    public List<Payment> findPaymentsByUserID(Long userID) {
        log.info("Fetching payments for user ID: {}", userID);
        return paymentRepository.findByUserID(userID);
    }
}
