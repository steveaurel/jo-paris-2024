package com.infoevent.ticketservice.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    private Long id;

    private String name;

    private String description;

    private OfferType offerType;

    private LocalDate date;

    private LocalTime time;

    private Long venueID;

    private int seatAvailable;
}
