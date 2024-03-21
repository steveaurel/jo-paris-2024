package com.infoevent.ticketservice.entities;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {

    private Long id;

    private BigDecimal amount;


}
