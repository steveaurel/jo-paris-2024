package com.infoevent.ticketservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OfferType {

    private Long id;

    private String description;

    private int seatQuantity;

    private Price price;
}
