package com.meetingplanner.repository;

import com.meetingplanner.domain.EquipementSalle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EquipementSalle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipementSalleRepository extends JpaRepository<EquipementSalle, Long> {}
