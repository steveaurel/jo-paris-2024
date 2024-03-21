package com.infoevent.eventservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OfferType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Min(1)
    @Column(nullable = false)
    private int seatQuantity;


    @OneToOne(mappedBy = "offerType", cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Price price;

}
