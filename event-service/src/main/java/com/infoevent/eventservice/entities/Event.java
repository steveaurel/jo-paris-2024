package com.infoevent.eventservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infoevent.eventservice.utils.EventDurationConverter;
import jakarta.persistence.*;
import lombok.*;

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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Column(nullable = false)
    @JsonFormat(pattern="HH:mm")
    private LocalTime time;

    @Column(nullable = false)
    @Convert(converter = EventDurationConverter.class)
    private EventDuration duration;                     //

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Price> prices = new HashSet<>();

    @Column(nullable = false)
    private Long venueID;

    @Transient
    private Venue venue;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Column(nullable = false)
    private int seatAvailable;


    public void addPrice(Price price) {
        prices.add(price);
        price.setEvent(this);
    }

    public void removePrice(Price price) {
        prices.remove(price);
        price.setEvent(null);
    }
}
