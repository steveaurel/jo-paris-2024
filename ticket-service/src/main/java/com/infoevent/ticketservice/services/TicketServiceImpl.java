package com.infoevent.ticketservice.services;

import com.infoevent.ticketservice.entities.Ticket;
import com.infoevent.ticketservice.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    @Override
    public Ticket createTicket(Ticket ticket) {
        log.info("Creating new ticket for user ID: {} and event ID: {}", ticket.getUserID(), ticket.getEventID());
        return ticketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> findTicketById(Long id) {
        log.info("Finding ticket by ID: {}", id);
        return ticketRepository.findById(id);
    }

    @Override
    public List<Ticket> findAllTickets() {
        log.info("Retrieving all tickets");
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> findTicketsByEventID(Long eventID) {
        log.info("Fetching tickets for event ID: {}", eventID);
        return ticketRepository.findByEventID(eventID);
    }

    @Override
    public List<Ticket> findTicketsByUserID(Long userID) {
        log.info("Fetching tickets for user ID: {}", userID);
        return ticketRepository.findByUserID(userID);
    }

    @Override
    public Optional<Ticket> findByQrCode(byte[] qrCode) {
        log.info("Finding ticket by qrCode: {}", qrCode);
        return ticketRepository.findByQrCode(qrCode);
    }
}
