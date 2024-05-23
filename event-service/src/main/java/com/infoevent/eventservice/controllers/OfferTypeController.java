package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.services.OfferTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offertypes")
@RequiredArgsConstructor
@Slf4j
public class OfferTypeController {

    private final OfferTypeService offerTypeService;

    /**
     * API endpoint for creating a new offer type.
     *
     * @param offerType The offer type to create, provided in the request body.
     * @return ResponseEntity containing the created offer type.
     */
    @PostMapping("")
    public ResponseEntity<OfferType> createOfferType(@Valid @RequestBody OfferType offerType) {
        OfferType createdOfferType = offerTypeService.createOfferType(offerType);
        return ResponseEntity.ok(createdOfferType);
    }

    /**
     * API endpoint for retrieving an offer type by its ID.
     *
     * @param id The ID of the offer type to find.
     * @return ResponseEntity containing the found offer type or NotFound.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfferType> findOfferTypeById(@PathVariable Long id) {
        return offerTypeService.findOfferTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * API endpoint for listing all offer types.
     *
     * @return ResponseEntity containing a list of all offer types.
     */
    @GetMapping("")
    public ResponseEntity<List<OfferType>> findAllOfferTypes() {
        List<OfferType> offerTypes = offerTypeService.findAllOfferTypes();
        return ResponseEntity.ok(offerTypes);
    }

    /**
     * API endpoint for updating an existing offer type.
     *
     * @param id        The ID of the offer type to update.
     * @param offerType The new offer type information, provided in the request body.
     * @return ResponseEntity containing the updated offer type.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOfferType(@PathVariable Long id, @Valid @RequestBody OfferType offerType) {
        try {
            OfferType updatedOfferType = offerTypeService.updateOfferType(id, offerType);
            return ResponseEntity.ok(updatedOfferType);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API endpoint for deleting an offer type by its ID.
     *
     * @param id The ID of the offer type to delete.
     * @return ResponseEntity with no content if the deletion was successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferType(@PathVariable Long id) {
        offerTypeService.deleteOfferType(id);
        return ResponseEntity.noContent().build();
    }
}

