package com.infoevent.notificationservice.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Implementation of the EmailService interface for sending emails with and without attachments.
 * This service utilizes Spring's JavaMailSender for the email functionality.
 */
@Service
@Async
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", to, e.getMessage());
            throw e;
        }
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfBytes) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Use true to indicate the content is HTML.

            // Attach the PDF document
            ByteArrayResource pdfResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("ticket.pdf", pdfResource);

            mailSender.send(message);
            log.info("Email with attachment sent to {}", to);
        } catch (MessagingException e) {
            log.error("Error sending email with attachment to {}: {}", to, e.getMessage());
            throw e;
        }
    }
}
