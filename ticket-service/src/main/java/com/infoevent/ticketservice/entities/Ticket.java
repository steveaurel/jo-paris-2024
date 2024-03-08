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
    private Long userId;

    @Column(nullable = false)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Column(nullable = false)
    private BigDecimal priceAmount;

    @Column(nullable = false)
    private String qrCode;
}
