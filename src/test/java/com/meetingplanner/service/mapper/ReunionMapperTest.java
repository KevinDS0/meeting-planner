package com.meetingplanner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReunionMapperTest {

    private ReunionMapper reunionMapper;

    @BeforeEach
    public void setUp() {
        reunionMapper = new ReunionMapperImpl();
    }
}
