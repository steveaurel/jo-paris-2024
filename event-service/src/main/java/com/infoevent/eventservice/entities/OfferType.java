package com.infoevent.eventservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OfferType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Min(1)
    @Column(nullable = false)
    private int seatQuantity;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    @ToString.Exclude
    private Event event;

    @OneToOne(mappedBy = "offerType", cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    @JsonManagedReference
    @ToString.Exclude
    private Price price;

}
