package com.infoevent.eventservice.repositories;

import com.infoevent.eventservice.entities.OfferType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OfferTypeRepositoryTest {

    @Autowired
    private OfferTypeRepository offerTypeRepository;

    @Test
    public void testSaveOfferType() {
        OfferType offerType = new OfferType();
        offerType.setDescription("Offer 1");
        offerType.setSeatQuantity(100);

        OfferType savedOfferType = offerTypeRepository.save(offerType);
        assertNotNull(savedOfferType.getId());
        assertEquals("Offer 1", savedOfferType.getDescription());
    }

    @Test
    public void testFindById() {
        OfferType offerType = new OfferType();
        offerType.setDescription("Offer 1");
        offerType.setSeatQuantity(100);

        OfferType savedOfferType = offerTypeRepository.save(offerType);
        Optional<OfferType> foundOfferType = offerTypeRepository.findById(savedOfferType.getId());
        assertTrue(foundOfferType.isPresent());
        assertEquals("Offer 1", foundOfferType.get().getDescription());
    }

    @Test
    public void testDeleteById() {
        OfferType offerType = new OfferType();
        offerType.setDescription("Offer 1");
        offerType.setSeatQuantity(100);

        OfferType savedOfferType = offerTypeRepository.save(offerType);
        offerTypeRepository.deleteById(savedOfferType.getId());
        Optional<OfferType> foundOfferType = offerTypeRepository.findById(savedOfferType.getId());
        assertFalse(foundOfferType.isPresent());
    }
}

