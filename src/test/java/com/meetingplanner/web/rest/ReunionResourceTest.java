package com.meetingplanner.web.rest;

import com.meetingplanner.domain.enumeration.Creneau;
import com.meetingplanner.domain.enumeration.TypeReunion;
import com.meetingplanner.service.dto.ReservationDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReunionResourceTest {

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private ReunionResource reunionResource;

    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(reunionResource).build();
    }

    @Test
    void shouldReturnOkWhenSalleReservee() throws Exception {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setTypeReunion(TypeReunion.RS);
        reservation.setNbParticipants(7);
        reservation.setCreneau(Creneau.C2);

        this.mockMvc.perform(post("/api/reunion/reserver")
            .content(TestUtil.convertObjectToJsonBytes(reservation))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(8));
    }

    @Test
    void shouldReturnNoContentWhenAucuneSalleDisponible() throws Exception {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setTypeReunion(TypeReunion.VC);
        reservation.setNbParticipants(28);
        reservation.setCreneau(Creneau.C2);

        this.mockMvc.perform(post("/api/reunion/reserver")
            .content(TestUtil.convertObjectToJsonBytes(reservation))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
