package com.infoevent.ticketservice.controllers;

import com.google.zxing.WriterException;
import com.infoevent.ticketservice.client.*;
import com.infoevent.ticketservice.entities.*;
import com.infoevent.ticketservice.services.TicketService;
import com.infoevent.ticketservice.utils.NotificationUtils;
import com.infoevent.ticketservice.utils.QRCodeUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * REST Controller for managing tickets.
 * Provides endpoints for creating and retrieving tickets,
 * as well as fetching tickets for a specific event and user.
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;
    private final KeyGeneratorRestClient keyGeneratorRestClient;
    private final UserRestClient userRestClient;
    private final EventRestClient eventRestClient;
    private final VenueRestClient venueRestClient;
    private final NotificationRestClient notificationRestClient;

    /**
     * Creates a new ticket.
     *
     * @param ticket The event to create, expected to be valid.
     * @return ResponseEntity containing the created event.
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) throws IOException, WriterException {
        try {
            log.info("API call to create new ticket for user ID: {} and event ID: {}", ticket.getUserID(), ticket.getEventID());


            String ticketKey = keyGeneratorRestClient.getKeyGenerator();
            ticket.setKey(ticketKey);


            User user = userRestClient.getUserById(ticket.getUserID());
            ticket.setLastName(user.getLastName());
            ticket.setFirstName(user.getFirstName());


            String qrCodeData = user.getKey().concat(ticket.getKey());
            byte[] qrCodeImage = QRCodeUtils.generateQRCodeImage(qrCodeData, 300, 300);
            ticket.setQrCode(qrCodeImage);


            Event event = eventRestClient.getEventById(ticket.getEventID());
            Venue venue = venueRestClient.getVenueById(event.getVenueID());
            OfferType offerType = eventRestClient.getOfferTypeById(ticket.getOfferTypeID());


            Ticket createdTicket = ticketService.createTicket(ticket);


            sendTicketNotification(ticket, offerType, event, venue);

            event.setSeatAvailable(event.getSeatAvailable() - offerType.getSeatQuantity());

            eventRestClient.updateEvent(event.getId(), event);

            return ResponseEntity.ok(createdTicket);
        } catch (Exception e) {
            log.error("Error creating ticket: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a ticket by its ID.
     *
     * @param id The ID of the ticket to retrieve.
     * @return ResponseEntity containing the requested ticket, if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findTicketById(@PathVariable Long id) {
        log.info("API call to find ticket by ID: {}", id);
        return ticketService.findTicketById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all tickets.
     *
     * @return ResponseEntity containing a list of all tickets.
     */
    @GetMapping
    public ResponseEntity<List<Ticket>> findAllTickets() {
        log.info("API call to list all tickets");
        List<Ticket> tickets = ticketService.findAllTickets();
        return ResponseEntity.ok(tickets);
    }

    /**
     * Retrieves tickets by a specific event ID.
     *
     * @param eventID The ID of the event to find tickets for.
     * @return ResponseEntity containing a list of tickets held at the specified event.
     */
    @GetMapping("/by-event/{eventID}")
    public ResponseEntity<List<Ticket>> findTicketsByEventID(@PathVariable Long eventID) {
        log.info("API call to fetch tickets for event ID: {}", eventID);
        List<Ticket> tickets = ticketService.findTicketsByEventID(eventID);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Retrieves tickets by a specific user ID.
     *
     * @param userID The ID of the user to find tickets for.
     * @return ResponseEntity containing a list of tickets held at the specified user.
     */
    @GetMapping("/by-user/{userID}")
    public ResponseEntity<List<Ticket>> findTicketsByUserID(@PathVariable Long userID) {
        log.info("API call to fetch tickets for user ID: {}", userID);
        List<Ticket> tickets = ticketService.findTicketsByUserID(userID);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Decodes a QR code image and verifies if it matches a stored ticket.
     * The method decodes the provided QR code image to a string and
     * attempts to match it with a combination of the user key and ticket key stored in the database.
     * It verifies the integrity and authenticity of the ticket QR code.
     *
     * @param qrCodeImage The QR code image as a byte array.
     * @return ResponseEntity with a Boolean indicating the success of the operation.
     */
    @PostMapping("/decode-qr")
    public ResponseEntity<Boolean> decodeQRCode(@RequestBody byte[] qrCodeImage) {
        log.info("API call to decode QR code and verify ticket authenticity");
        try {
            String decodedData = QRCodeUtils.decodeQRCode(qrCodeImage);
            Ticket ticket = ticketService.findByQrCode(qrCodeImage).orElse(null);

            if (ticket == null) {
                log.info("No ticket found matching the provided QR code");
                return ResponseEntity.notFound().build();
            }

            User user = userRestClient.getUserById(ticket.getUserID());
            boolean success = decodedData.equals(user.getKey().concat(ticket.getKey()));

            log.info("QR code decoded successfully for ticket ID: {}, Verification status: {}", ticket.getId(), success);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            log.error("Error decoding QR code: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    private void sendTicketNotification(Ticket ticket, OfferType offerType, Event event, Venue venue) throws IOException, WriterException {
        NotificationRequest notificationRequest = NotificationUtils.buildNotificationRequest(ticket, offerType, event, venue);
        notificationRestClient.sendNotification(notificationRequest);
    }
}
