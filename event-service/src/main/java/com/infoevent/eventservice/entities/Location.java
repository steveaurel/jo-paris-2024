package com.infoevent.eventservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    Long id;
    String address;
    String city;
    String country;
}
