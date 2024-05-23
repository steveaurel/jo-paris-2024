package com.infoevent.eventservice.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "venue")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<OfferType> offerTypes = new HashSet<>();

    @Column(nullable = false)
    private Long venueID;

    @Transient
    private Venue venue;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Column(nullable = false)
    private int seatAvailable;

}
