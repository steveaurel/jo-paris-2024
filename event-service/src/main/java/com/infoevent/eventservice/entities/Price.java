package com.infoevent.eventservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(1)
    private BigDecimal amount;

    private String currency;

    @OneToOne
    @JoinColumn(name = "offer_type_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private OfferType offerType;
}
