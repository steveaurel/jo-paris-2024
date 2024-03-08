package com.infoevent.paymentservice.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDetails {
    private Long eventId;
    private String TypeTicket;
}
