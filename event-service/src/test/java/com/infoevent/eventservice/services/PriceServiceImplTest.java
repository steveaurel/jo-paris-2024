package com.infoevent.eventservice.services;

import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.repositories.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceServiceImpl priceService;

    private Price price;

    @BeforeEach
    public void setUp() {
        price = Price.builder()
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD")
                .build();
    }

    @Test
    public void testCreatePrice() {
        when(priceRepository.save(any(Price.class))).thenReturn(price);

        Price createdPrice = priceService.createPrice(price);
        assertNotNull(createdPrice);
        assertEquals(BigDecimal.valueOf(99.99), createdPrice.getAmount());
        verify(priceRepository, times(1)).save(price);
    }

    @Test
    public void testFindPriceById() {
        when(priceRepository.findById(1L)).thenReturn(Optional.of(price));

        Optional<Price> foundPrice = priceService.findPriceById(1L);
        assertTrue(foundPrice.isPresent());
        assertEquals(BigDecimal.valueOf(99.99), foundPrice.get().getAmount());
        verify(priceRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAllPrices() {
        Price price2 = Price.builder()
                .amount(BigDecimal.valueOf(199.99))
                .currency("USD")
                .build();

        when(priceRepository.findAll()).thenReturn(Arrays.asList(price, price2));

        List<Price> prices = priceService.findAllPrices();
        assertEquals(2, prices.size());
        verify(priceRepository, times(1)).findAll();
    }

    @Test
    public void testUpdatePrice() {
        Price updatedPrice = Price.builder()
                .amount(BigDecimal.valueOf(149.99))
                .currency("USD")
                .build();

        when(priceRepository.findById(1L)).thenReturn(Optional.of(price));
        when(priceRepository.save(any(Price.class))).thenReturn(updatedPrice);

        Price result = priceService.updatePrice(1L, updatedPrice);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(149.99), result.getAmount());
        verify(priceRepository, times(1)).findById(1L);
        verify(priceRepository, times(1)).save(any(Price.class));
    }

    @Test
    public void testUpdatePriceNotFound() {
        Price updatedPrice = Price.builder()
                .amount(BigDecimal.valueOf(149.99))
                .currency("USD")
                .build();

        when(priceRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            priceService.updatePrice(1L, updatedPrice);
        });

        assertEquals("Price not found with ID: 1", exception.getMessage());
        verify(priceRepository, times(1)).findById(1L);
        verify(priceRepository, never()).save(any(Price.class));
    }

    @Test
    public void testDeletePrice() {
        doNothing().when(priceRepository).deleteById(1L);

        priceService.deletePrice(1L);
        verify(priceRepository, times(1)).deleteById(1L);
    }
}
