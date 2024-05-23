package com.infoevent.eventservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Location {
    Long id;
    String address;
    String city;
    String country;
}
