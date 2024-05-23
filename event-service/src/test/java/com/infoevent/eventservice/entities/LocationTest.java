package com.infoevent.eventservice.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    @Test
    public void testLocationGettersAndSetters() {
        Location location = new Location();
        location.setId(1L);
        location.setAddress("123 Main St");
        location.setCity("Paris");
        location.setCountry("France");

        assertEquals(1L, location.getId());
        assertEquals("123 Main St", location.getAddress());
        assertEquals("Paris", location.getCity());
        assertEquals("France", location.getCountry());
    }

    @Test
    public void testLocationToString() {
        Location location = Location.builder()
                .id(1L)
                .address("123 Main St")
                .city("Paris")
                .country("France")
                .build();

        String expected = "Location(id=1, address=123 Main St, city=Paris, country=France)";
        assertEquals(expected, location.toString());
    }

    @Test
    public void testLocationBuilderToString() {
        Location.LocationBuilder builder = Location.builder()
                .id(1L)
                .address("123 Main St")
                .city("Paris")
                .country("France");

        String expected = "Location.LocationBuilder(id=1, address=123 Main St, city=Paris, country=France)";
        assertEquals(expected, builder.toString());
    }
}
