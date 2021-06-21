package com.meetingplanner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipementSalleMapperTest {

    private EquipementSalleMapper equipementSalleMapper;

    @BeforeEach
    public void setUp() {
        equipementSalleMapper = new EquipementSalleMapperImpl();
    }
}
