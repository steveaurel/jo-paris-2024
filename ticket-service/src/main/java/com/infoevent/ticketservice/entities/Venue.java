package com.infoevent.ticketservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    private Long id;
    private String name;
    private int capacity;
    private Location location;
}
