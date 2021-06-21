package com.meetingplanner.repository;

import com.meetingplanner.domain.EquipementLibre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EquipementLibre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipementLibreRepository extends JpaRepository<EquipementLibre, Long> {}
