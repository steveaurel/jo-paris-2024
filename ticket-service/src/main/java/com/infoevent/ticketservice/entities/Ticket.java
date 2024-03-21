package com.infoevent.ticketservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long eventID;

    @Column(nullable = false)
    private Long paymentID;

    @Column(nullable = false)
    private Long offerTypeID;

    @Column(nullable = false)
    private BigDecimal priceAmount;

    @Column(nullable = false)
    @Lob
    private byte[] qrCode;

    @Column(nullable = false, unique = true)
    private String key;
}
