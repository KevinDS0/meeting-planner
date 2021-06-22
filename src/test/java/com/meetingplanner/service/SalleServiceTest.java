package com.meetingplanner.service;

import com.meetingplanner.domain.Salle;
import com.meetingplanner.repository.SalleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SalleServiceTest {

    @Autowired
    private SalleService salleService;

    @Mock
    private SalleRepository salleRepository;

    @BeforeEach
    void before() {
        ReflectionTestUtils.setField(salleService, "salleRepository", salleRepository);
    }

    @Test
    void shouldReturnListeSalleAdapteesWhenCapaciteIsAdaptee() {
        Integer nbParticipants = 5;
        Salle salle1 = new Salle();
        salle1.setId(1L);
        salle1.setCapacite(12);

        Salle salle2 = new Salle();
        salle2.setId(2L);
        salle2.setCapacite(5);
        Mockito.when(salleRepository.findAll()).thenReturn(List.of(salle1, salle2));

        Set<Salle> sallesAdaptees = salleService.getSalleCapaciteAdaptee(nbParticipants);
        List<Salle> listeSalle = new ArrayList<>(sallesAdaptees);

        Assertions.assertTrue(CollectionUtils.isNotEmpty(sallesAdaptees));
        Assertions.assertEquals(1, sallesAdaptees.size());
        Assertions.assertEquals(1, listeSalle.get(0).getId());
    }

    @Test
    void shouldAucuneSallesWhenCapaciteIsNotAdaptee() {
        Integer nbParticipants = 15;
        Salle salle1 = new Salle();
        salle1.setId(1L);
        salle1.setCapacite(12);

        Salle salle2 = new Salle();
        salle2.setId(2L);
        salle2.setCapacite(5);
        Mockito.when(salleRepository.findAll()).thenReturn(List.of(salle1, salle2));

        Set<Salle> sallesAdaptees = salleService.getSalleCapaciteAdaptee(nbParticipants);

        Assertions.assertTrue(CollectionUtils.isEmpty(sallesAdaptees));
    }

    @Test
    void verifierEquipementsSalle() {
    }
}
