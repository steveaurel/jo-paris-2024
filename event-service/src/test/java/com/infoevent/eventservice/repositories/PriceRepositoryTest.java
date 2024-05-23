package com.infoevent.eventservice.repositories;

import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.repositories.OfferTypeRepository;
import com.infoevent.eventservice.repositories.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private OfferTypeRepository offerTypeRepository;

    private OfferType offerType;

    @BeforeEach
    public void setUp() {
        offerType = OfferType.builder()
                .description("Offer 1")
                .seatQuantity(100)
                .build();
        offerType = offerTypeRepository.save(offerType);
    }

    @Test
    public void testSavePrice() {
        Price price = Price.builder()
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD")
                .offerType(offerType)
                .build();

        Price savedPrice = priceRepository.save(price);
        assertNotNull(savedPrice.getId());
        assertEquals(BigDecimal.valueOf(99.99), savedPrice.getAmount());
        assertEquals(offerType.getId(), savedPrice.getOfferType().getId());
    }

    @Test
    public void testFindById() {
        Price price = Price.builder()
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD")
                .offerType(offerType)
                .build();

        Price savedPrice = priceRepository.save(price);
        Optional<Price> foundPrice = priceRepository.findById(savedPrice.getId());
        assertTrue(foundPrice.isPresent());
        assertEquals(BigDecimal.valueOf(99.99), foundPrice.get().getAmount());
        assertEquals(offerType.getId(), foundPrice.get().getOfferType().getId());
    }

    @Test
    public void testDeleteById() {
        Price price = Price.builder()
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD")
                .offerType(offerType)
                .build();

        Price savedPrice = priceRepository.save(price);
        priceRepository.deleteById(savedPrice.getId());
        Optional<Price> foundPrice = priceRepository.findById(savedPrice.getId());
        assertFalse(foundPrice.isPresent());
    }
}
