package com.infoevent.eventservice.entities;

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
