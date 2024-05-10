package com.infoevent.ticketservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private String eventName;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyy")
    private LocalDate eventDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime eventStartTime;

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
