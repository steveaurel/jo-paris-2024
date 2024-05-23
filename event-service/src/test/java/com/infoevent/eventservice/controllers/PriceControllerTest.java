package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.entities.Price;
import com.infoevent.eventservice.services.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceController.class)
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    private Price price;
    private OfferType offerType;

    @BeforeEach
    public void setUp() {
        offerType = OfferType.builder()
                .id(1L)
                .description("Offer 1")
                .seatQuantity(100)
                .build();

        price = Price.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(99.99))
                .currency("USD")
                .offerType(offerType)
                .build();
    }

    @Test
    public void testCreatePrice() throws Exception {
        when(priceService.createPrice(any(Price.class))).thenReturn(price);

        mockMvc.perform(post("/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"amount\": 99.99, \"currency\": \"USD\", \"offerType\": { \"id\": 1, \"description\": \"Offer 1\", \"seatQuantity\": 100 } }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(99.99));

        verify(priceService, times(1)).createPrice(any(Price.class));
    }

    @Test
    public void testFindPriceById() throws Exception {
        when(priceService.findPriceById(anyLong())).thenReturn(Optional.of(price));

        mockMvc.perform(get("/prices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(99.99));

        verify(priceService, times(1)).findPriceById(1L);
    }

    @Test
    public void testFindPriceByIdNotFound() throws Exception {
        when(priceService.findPriceById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/prices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(priceService, times(1)).findPriceById(1L);
    }

    @Test
    public void testFindAllPrices() throws Exception {
        when(priceService.findAllPrices()).thenReturn(Arrays.asList(price));

        mockMvc.perform(get("/prices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(99.99));

        verify(priceService, times(1)).findAllPrices();
    }

    @Test
    public void testUpdatePrice() throws Exception {
        when(priceService.updatePrice(anyLong(), any(Price.class))).thenReturn(price);

        mockMvc.perform(put("/prices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"amount\": 150.00, \"currency\": \"USD\", \"offerType\": { \"id\": 1, \"description\": \"Offer 1\", \"seatQuantity\": 100 } }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(99.99));

        verify(priceService, times(1)).updatePrice(anyLong(), any(Price.class));
    }

    @Test
    public void testDeletePrice() throws Exception {
        doNothing().when(priceService).deletePrice(anyLong());

        mockMvc.perform(delete("/prices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(priceService, times(1)).deletePrice(1L);
    }
}
