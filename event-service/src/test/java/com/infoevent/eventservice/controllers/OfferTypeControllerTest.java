package com.infoevent.eventservice.controllers;

import com.infoevent.eventservice.entities.OfferType;
import com.infoevent.eventservice.services.OfferTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OfferTypeController.class)
public class OfferTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferTypeService offerTypeService;

    private OfferType offerType;

    @BeforeEach
    public void setUp() {
        offerType = new OfferType();
        offerType.setId(1L);
        offerType.setDescription("Offer 1");
        offerType.setSeatQuantity(100);
    }

    @Test
    public void testCreateOfferType() throws Exception {
        when(offerTypeService.createOfferType(any(OfferType.class))).thenReturn(offerType);

        mockMvc.perform(post("/offertypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"Offer 1\", \"seatQuantity\": 100 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Offer 1"));

        verify(offerTypeService, times(1)).createOfferType(any(OfferType.class));
    }

    @Test
    public void testFindOfferTypeById() throws Exception {
        when(offerTypeService.findOfferTypeById(anyLong())).thenReturn(Optional.of(offerType));

        mockMvc.perform(get("/offertypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Offer 1"));

        verify(offerTypeService, times(1)).findOfferTypeById(1L);
    }

    @Test
    public void testFindOfferTypeByIdNotFound() throws Exception {
        when(offerTypeService.findOfferTypeById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/offertypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(offerTypeService, times(1)).findOfferTypeById(1L);
    }

    @Test
    public void testFindAllOfferTypes() throws Exception {
        when(offerTypeService.findAllOfferTypes()).thenReturn(Arrays.asList(offerType));

        mockMvc.perform(get("/offertypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Offer 1"));

        verify(offerTypeService, times(1)).findAllOfferTypes();
    }

    @Test
    public void testUpdateOfferType() throws Exception {
        when(offerTypeService.updateOfferType(anyLong(), any(OfferType.class))).thenReturn(offerType);

        mockMvc.perform(put("/offertypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"Updated Offer\", \"seatQuantity\": 150 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Offer 1"));

        verify(offerTypeService, times(1)).updateOfferType(anyLong(), any(OfferType.class));
    }

    @Test
    public void testUpdateOfferTypeNotFound() throws Exception {
        when(offerTypeService.updateOfferType(anyLong(), any(OfferType.class)))
                .thenThrow(new IllegalStateException("OfferType not found with ID: 1"));

        mockMvc.perform(put("/offertypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"description\": \"Updated Offer\", \"seatQuantity\": 150 }"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("OfferType not found with ID: 1"));

        verify(offerTypeService, times(1)).updateOfferType(anyLong(), any(OfferType.class));
    }


    @Test
    public void testDeleteOfferType() throws Exception {
        doNothing().when(offerTypeService).deleteOfferType(anyLong());

        mockMvc.perform(delete("/offertypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(offerTypeService, times(1)).deleteOfferType(1L);
    }
}
