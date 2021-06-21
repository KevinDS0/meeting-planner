package com.meetingplanner.repository;

import com.meetingplanner.domain.Reunion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reunion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Long> {}
