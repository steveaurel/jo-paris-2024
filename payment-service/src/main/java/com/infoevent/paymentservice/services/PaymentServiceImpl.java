package com.infoevent.paymentservice.services;

import com.infoevent.paymentservice.entities.Payment;
import com.infoevent.paymentservice.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    @Override
    @Transactional()
    public Payment createPayment(Payment payment) {
        log.info("Creating new payment for user ID: {} and event ID: {}", payment.getUserID(), payment.getEventID());
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findPaymentById(Long id) {
        log.info("Finding payment by ID: {}", id);
        return paymentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAllPayments() {
        log.info("Retrieving all payments");
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findPaymentsByEventID(Long eventID) {
        log.info("Fetching payments for event ID: {}", eventID);
        return paymentRepository.findByEventID(eventID);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findPaymentsByUserID(Long userID) {
        log.info("Fetching payments for user ID: {}", userID);
        return paymentRepository.findByUserID(userID);
    }
}
