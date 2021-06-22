package com.meetingplanner.repository;

import com.meetingplanner.domain.Salle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data SQL repository for the Salle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {

    @Query("from Salle s left join Reunion r on r.salle.id = s.id where r.salle is null ")
    Optional<Set<Salle>> findAllAvailableSalle();
}
