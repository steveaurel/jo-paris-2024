package com.infoevent.eventservice.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OfferTypeTest {

    @Test
    public void testOfferTypeGettersAndSetters() {
        OfferType offerType = new OfferType();
        offerType.setId(1L);
        offerType.setDescription("Offer Description");
        offerType.setSeatQuantity(100);
        offerType.setEvent(new Event());
        offerType.setPrice(new Price());

        assertEquals(1L, offerType.getId());
        assertEquals("Offer Description", offerType.getDescription());
        assertEquals(100, offerType.getSeatQuantity());
        assertNotNull(offerType.getEvent());
        assertNotNull(offerType.getPrice());
    }

    @Test
    public void testOfferTypeToString() {
        OfferType offerType = new OfferType();
        offerType.setId(1L);
        offerType.setDescription("Offer Description");
        offerType.setSeatQuantity(100);

        String expectedToString = "OfferType(id=1, description=Offer Description, seatQuantity=100)";
        assertEquals(expectedToString, offerType.toString());
    }

    @Test
    public void testSetEvent() {
        OfferType offerType = new OfferType();
        Event event = new Event();
        offerType.setEvent(event);

        assertEquals(event, offerType.getEvent());
    }

    @Test
    public void testSetPrice() {
        OfferType offerType = new OfferType();
        Price price = new Price();
        offerType.setPrice(price);

        assertEquals(price, offerType.getPrice());
    }

    @Test
    public void testOfferTypeBuilderToString() {
        OfferType.OfferTypeBuilder builder = OfferType.builder()
                .id(1L)
                .description("Offer Description")
                .seatQuantity(100)
                .event(new Event())
                .price(new Price());

        String builderToString = builder.toString();
        assertTrue(builderToString.contains("OfferType.OfferTypeBuilder"));
        assertTrue(builderToString.contains("id=1"));
        assertTrue(builderToString.contains("description=Offer Description"));
        assertTrue(builderToString.contains("seatQuantity=100"));

    }
}
