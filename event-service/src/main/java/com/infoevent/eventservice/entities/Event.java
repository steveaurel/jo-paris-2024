package com.infoevent.eventservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private Duration duration;
    private Long venueID;
    @Transient
    private Venue venue;
}

