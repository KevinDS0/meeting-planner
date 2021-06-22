package com.meetingplanner.repository;

import com.meetingplanner.domain.Salle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SalleRepositoryTest {

    @Autowired
    private SalleRepository salleRepository;

    @BeforeEach
    public void beforeEach() {
    }

    @Test
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/sql/insert-reunion.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/sql/clean-reunion.sql")
    })
    void findAllAvailableSalle() {
        Optional<Set<Salle>> sallesDisponibles = salleRepository.findAllAvailableSalle();

        assertTrue(sallesDisponibles.isPresent());
        assertFalse(sallesDisponibles.get().isEmpty());
        assertEquals(11, sallesDisponibles.get().size());
    }
}
