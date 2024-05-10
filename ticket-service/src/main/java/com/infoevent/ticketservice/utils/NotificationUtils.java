package com.infoevent.ticketservice.utils;

import com.infoevent.ticketservice.entities.*;

public class NotificationUtils {
    public static NotificationRequest buildNotificationRequest(Ticket ticket, OfferType offerType, Event event, Venue venue) {
        return NotificationRequest.builder()
                .firstName(ticket.getFirstName())
                .lastName(ticket.getLastName())
                .ticketType(offerType.getDescription())
                .price(offerType.getPrice().getAmount() + " €")
                .qrCode(ticket.getQrCode())
                .eventName(event.getName())
                .dateTime(event.getDate().toString() + " " + event.getStartTime().toString())
                .venueName(venue.getName())
                .location(venue.getLocation().getAddress() + ", " + venue.getLocation().getCity() + ", " + venue.getLocation().getCountry())
                .build();
    }
}
