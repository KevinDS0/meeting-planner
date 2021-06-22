package com.meetingplanner.service;

import com.meetingplanner.domain.Reunion;
import com.meetingplanner.domain.Salle;
import com.meetingplanner.domain.enumeration.Creneau;
import com.meetingplanner.domain.enumeration.TypeReunion;
import com.meetingplanner.repository.ReunionRepository;
import com.meetingplanner.service.dto.ReservationDTO;
import com.meetingplanner.service.dto.SalleDTO;
import com.meetingplanner.service.mapper.SalleMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReunionServiceTest {

    @Mock
    private SalleService salleService;

    @Mock
    private ReunionRepository reunionRepository;

    @Autowired
    private ReunionService reunionService;

    @BeforeEach
    void before() {
        ReflectionTestUtils.setField(reunionService, "salleService", salleService);
        ReflectionTestUtils.setField(reunionService, "reunionRepository", reunionRepository);
    }

    @Test
    void shouldReturnNoSuchElementExceptionWhenAucuneSalleDisponible() {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setTypeReunion(TypeReunion.VC);
        reservation.setNbParticipants(6);
        reservation.setCreneau(Creneau.C2);

        Reunion reunion1 = new Reunion();
        reunion1.setCreneau(Creneau.C2);
        Salle salle1 = new Salle();
        salle1.setId(1L);
        salle1.setCapacite(12);
        reunion1.setSalle(salle1);

        Reunion reunion2 = new Reunion();
        reunion2.setCreneau(Creneau.C1);
        Salle salle2 = new Salle();
        salle2.setId(2L);
        salle2.setCapacite(15);
        reunion2.setSalle(salle2);

        Set<Reunion> reunions = new HashSet<>(Set.of(reunion1, reunion2));

        Mockito.when(reunionRepository.findAllReunionsByCreneauIn(Set.of(Creneau.C2, Creneau.C1)))
            .thenReturn(reunions);

        Mockito.when(salleService.getSalleCapaciteAdaptee(reservation.getNbParticipants())).thenReturn(Set.of(salle1, salle2));
        Mockito.when(salleService.verifierEquipementsSalle(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
           reunionService.getSalleAdapteeDisponible(reservation);
        });

        Assertions.assertEquals("Aucune salle disponible", exception.getMessage());
    }

    @Test
    void shouldReturnSalleWhenSalleDisponible() {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setTypeReunion(TypeReunion.VC);
        reservation.setNbParticipants(6);
        reservation.setCreneau(Creneau.C2);

        Reunion reunion1 = new Reunion();
        reunion1.setCreneau(Creneau.C2);
        Salle salle1 = new Salle();
        salle1.setId(1L);
        salle1.setCapacite(12);
        reunion1.setSalle(salle1);

        Reunion reunion2 = new Reunion();
        reunion2.setCreneau(Creneau.C1);
        Salle salle2 = new Salle();
        salle2.setId(2L);
        salle2.setCapacite(15);
        reunion2.setSalle(salle2);

        Mockito.when(reunionRepository.findAllReunionsByCreneauIn(Set.of(Creneau.C2, Creneau.C1)))
            .thenReturn(new HashSet<>());

        Mockito.when(salleService.getSalleCapaciteAdaptee(reservation.getNbParticipants())).thenReturn(Set.of(salle1, salle2));
        Mockito.when(salleService.verifierEquipementsSalle(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        SalleDTO result = reunionService.getSalleAdapteeDisponible(reservation);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(12, result.getCapacite());
    }
}
