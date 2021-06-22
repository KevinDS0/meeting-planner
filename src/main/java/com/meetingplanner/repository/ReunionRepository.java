package com.meetingplanner.repository;

import com.meetingplanner.domain.Reunion;
import com.meetingplanner.domain.enumeration.Creneau;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Spring Data SQL repository for the Reunion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Long> {

    Set<Reunion> findAllReunionsByCreneauIn(Set<Creneau> creneaux);

}
