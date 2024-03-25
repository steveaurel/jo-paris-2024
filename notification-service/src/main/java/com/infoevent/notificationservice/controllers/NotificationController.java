package com.infoevent.notificationservice.controllers;

import com.infoevent.notificationservice.entities.NotificationRequest;
import com.infoevent.notificationservice.services.NotificationService;
import com.infoevent.notificationservice.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling notification requests.
 * Supports operations for sending emails with or without attachments.
 */
@RestController
@EnableAsync
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Sends a simple email notification.
     *
     * @param notificationRequest The request containing notification details.
     * @return A response entity indicating the outcome of the send operation.
     */
    @PostMapping("/register")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody NotificationRequest notificationRequest) {
        log.info("Attempting to send email to {}", notificationRequest.getEmail());
        try {
            String subject = "Bienvenue sur la billeterie des J.0 2024 !";
            String body = Utils.constructWelcomeEmailBody(notificationRequest);
            notificationService.sendEmail(notificationRequest.getEmail(), subject, body);
            log.info("Email successfully sent to {}", notificationRequest.getEmail());
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", notificationRequest.getEmail(), e.getMessage());
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sends an email notification with an attached PDF.
     *
     * @param notificationRequest The request containing notification and attachment details.
     * @return A response entity indicating the outcome of the send operation.
     */
    @PostMapping("/confirmation")
    public ResponseEntity<String> sendEmailWithAttachment(@Valid @RequestBody NotificationRequest notificationRequest) {
        log.info("Attempting to send email with attachment to {}", notificationRequest.getEmail());
        try {
            String subject = "Confirmation de votre achat de billet";
            String body = Utils.constructConfirmationEmailBody(notificationRequest);
            byte[] pdfBytes = Utils.createTicketPdf(notificationRequest);
            notificationService.sendEmailWithAttachment(notificationRequest.getEmail(), subject, body, pdfBytes);
            log.info("Email with attachment successfully sent to {}", notificationRequest.getEmail());
            return ResponseEntity.ok("Email with attachment sent successfully");
        } catch (Exception e) {
            log.error("Error sending email with attachment to {}: {}", notificationRequest.getEmail(), e.getMessage(), e);
            return new ResponseEntity<>("Error sending email with attachment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
