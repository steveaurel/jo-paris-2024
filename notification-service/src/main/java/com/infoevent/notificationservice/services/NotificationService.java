package com.infoevent.notificationservice.services;

import jakarta.mail.MessagingException;

/**
 * The EmailService interface defines the operations for sending emails.
 * It provides functionality to send simple text emails as well as emails with attachments.
 */
public interface NotificationService {

    /**
     * Sends an email with the specified subject and body to the given recipient.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param body The body of the email.
     */
    public void sendEmail(String to, String subject, String body);

    /**
     * Sends an email with the specified subject and text content to the given recipient,
     * including a PDF attachment.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param text The text content of the email.
     * @param pdfBytes The byte array representing the PDF document to be attached.
     * @throws MessagingException If there is a failure in the email sending process.
     */
    void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfBytes) throws MessagingException;
}
