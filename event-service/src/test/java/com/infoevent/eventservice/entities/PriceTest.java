package com.infoevent.eventservice.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PriceTest {

    @Test
    public void testPriceGettersAndSetters() {
        Price price = new Price();
        price.setId(1L);
        price.setAmount(BigDecimal.valueOf(99.99));
        price.setCurrency("USD");
        price.setOfferType(new OfferType());

        assertEquals(1L, price.getId());
        assertEquals(BigDecimal.valueOf(99.99), price.getAmount());
        assertEquals("USD", price.getCurrency());
        assertNotNull(price.getOfferType());
    }



    @Test
    public void testPriceToString() {
        Price price = Price.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD")
                .build();

        String expectedToString = "Price(id=1, amount=99.99, currency=USD)"; // Adjust as needed for OfferType
        assertEquals(expectedToString, price.toString());
    }

    @Test
    public void testPriceBuilderToString() {
        Price.PriceBuilder builder = Price.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD");

        String builderToString = builder.toString();
        assertTrue(builderToString.contains("Price.PriceBuilder"));
        assertTrue(builderToString.contains("id=1"));
        assertTrue(builderToString.contains("amount=99.99"));
        assertTrue(builderToString.contains("currency=USD"));
    }
}
