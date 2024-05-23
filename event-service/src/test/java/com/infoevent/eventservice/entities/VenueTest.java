package com.infoevent.eventservice.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VenueTest {

    @Test
    public void testVenueGettersAndSetters() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("Olympic Stadium");
        venue.setCapacity(50000);

        Location location = new Location();
        location.setId(1L);
        location.setAddress("123 Main St");
        location.setCity("Paris");
        location.setCountry("France");

        venue.setLocation(location);

        assertEquals(1L, venue.getId());
        assertEquals("Olympic Stadium", venue.getName());
        assertEquals(50000, venue.getCapacity());
        assertNotNull(venue.getLocation());
        assertEquals(location, venue.getLocation());
    }

    @Test
    public void testVenueToString() {
        Location location = Location.builder()
                .id(1L)
                .address("123 Main St")
                .city("Paris")
                .country("France")
                .build();

        Venue venue = Venue.builder()
                .id(1L)
                .name("Olympic Stadium")
                .capacity(50000)
                .location(location)
                .build();

        String expected = "Venue(id=1, name=Olympic Stadium, capacity=50000)";
        assertEquals(expected, venue.toString());
    }

    @Test
    public void testVenueBuilderToString() {
        Location.LocationBuilder locationBuilder = Location.builder()
                .id(1L)
                .address("123 Main St")
                .city("Paris")
                .country("France");

        Venue.VenueBuilder builder = Venue.builder()
                .id(1L)
                .name("Olympic Stadium")
                .capacity(50000)
                .location(locationBuilder.build());

        String expected = "Venue.VenueBuilder(id=1, name=Olympic Stadium, capacity=50000, location=" + locationBuilder.build() + ")";
        assertEquals(expected, builder.toString());
    }
}
