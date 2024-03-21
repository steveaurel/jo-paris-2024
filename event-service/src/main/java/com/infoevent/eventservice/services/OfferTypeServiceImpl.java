package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.repositories.OfferTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferTypeServiceImpl implements OfferTypeService {

    private final OfferTypeRepository offerTypeRepository;

    @Override
    public OfferType createOfferType(OfferType offerType) {
        log.info("Creating new offer type: {}", offerType.getDescription());
        return offerTypeRepository.save(offerType);
    }

    @Override
    public Optional<OfferType> findOfferTypeById(Long id) {
        log.info("Finding offer type by ID: {}", id);
        return offerTypeRepository.findById(id);
    }

    @Override
    public List<OfferType> findAllOfferTypes() {
        log.info("Retrieving all offer types");
        return offerTypeRepository.findAll();
    }

    @Override
    public OfferType updateOfferType(Long id, OfferType offerType) {
        log.info("Updating offer type with ID: {}", id);
        return offerTypeRepository.findById(id)
                .map(existingOfferType -> {
                    existingOfferType.setDescription(offerType.getDescription());
                    existingOfferType.setSeatQuantity(offerType.getSeatQuantity());
                    return offerTypeRepository.save(existingOfferType);
                }).orElseThrow(() -> new IllegalStateException("OfferType not found with ID: " + id));
    }

    @Override
    public void deleteOfferType(Long id) {
        log.info("Deleting offer type with ID: {}", id);
        offerTypeRepository.deleteById(id);
    }
}
