package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.repositories.OfferTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferTypeServiceImplTest {

    @Mock
    private OfferTypeRepository offerTypeRepository;

    @InjectMocks
    private OfferTypeServiceImpl offerTypeService;

    private OfferType offerType;

    @BeforeEach
    public void setUp() {
        offerType = new OfferType();
        offerType.setDescription("Offer 1");
        offerType.setSeatQuantity(100);
    }

    @Test
    public void testCreateOfferType() {
        when(offerTypeRepository.save(any(OfferType.class))).thenReturn(offerType);

        OfferType createdOfferType = offerTypeService.createOfferType(offerType);
        assertNotNull(createdOfferType);
        assertEquals("Offer 1", createdOfferType.getDescription());
        verify(offerTypeRepository, times(1)).save(offerType);
    }

    @Test
    public void testFindOfferTypeById() {
        when(offerTypeRepository.findById(1L)).thenReturn(Optional.of(offerType));

        Optional<OfferType> foundOfferType = offerTypeService.findOfferTypeById(1L);
        assertTrue(foundOfferType.isPresent());
        assertEquals("Offer 1", foundOfferType.get().getDescription());
        verify(offerTypeRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAllOfferTypes() {
        OfferType offerType2 = new OfferType();
        offerType2.setDescription("Offer 2");
        offerType2.setSeatQuantity(200);

        when(offerTypeRepository.findAll()).thenReturn(Arrays.asList(offerType, offerType2));

        List<OfferType> offerTypes = offerTypeService.findAllOfferTypes();
        assertEquals(2, offerTypes.size());
        verify(offerTypeRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateOfferType() {
        OfferType updatedOfferType = new OfferType();
        updatedOfferType.setDescription("Updated Offer");
        updatedOfferType.setSeatQuantity(150);

        when(offerTypeRepository.findById(1L)).thenReturn(Optional.of(offerType));
        when(offerTypeRepository.save(any(OfferType.class))).thenReturn(updatedOfferType);

        OfferType result = offerTypeService.updateOfferType(1L, updatedOfferType);
        assertNotNull(result);
        assertEquals("Updated Offer", result.getDescription());
        verify(offerTypeRepository, times(1)).findById(1L);
        verify(offerTypeRepository, times(1)).save(any(OfferType.class));
    }

    @Test
    public void testUpdateOfferTypeNotFound() {
        when(offerTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            offerTypeService.updateOfferType(1L, offerType);
        });

        assertEquals("OfferType not found with ID: 1", exception.getMessage());
        verify(offerTypeRepository, times(1)).findById(1L);
        verify(offerTypeRepository, never()).save(any(OfferType.class));
    }

    @Test
    public void testDeleteOfferType() {
        doNothing().when(offerTypeRepository).deleteById(1L);

        offerTypeService.deleteOfferType(1L);
        verify(offerTypeRepository, times(1)).deleteById(1L);
    }
}
