package com.infoevent.venueservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private int capacity;
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
}
