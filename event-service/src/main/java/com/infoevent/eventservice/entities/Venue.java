package com.infoevent.eventservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Venue {
    private Long id;
    private String name;
    private int capacity;
    @ToString.Exclude
    private Location location;
}
