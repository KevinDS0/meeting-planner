package com.meetingplanner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalleMapperTest {

    private SalleMapper salleMapper;

    @BeforeEach
    public void setUp() {
        salleMapper = new SalleMapperImpl();
    }
}
