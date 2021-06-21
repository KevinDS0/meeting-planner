package com.meetingplanner.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetingplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReunionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReunionDTO.class);
        ReunionDTO reunionDTO1 = new ReunionDTO();
        reunionDTO1.setId(1L);
        ReunionDTO reunionDTO2 = new ReunionDTO();
        assertThat(reunionDTO1).isNotEqualTo(reunionDTO2);
        reunionDTO2.setId(reunionDTO1.getId());
        assertThat(reunionDTO1).isEqualTo(reunionDTO2);
        reunionDTO2.setId(2L);
        assertThat(reunionDTO1).isNotEqualTo(reunionDTO2);
        reunionDTO1.setId(null);
        assertThat(reunionDTO1).isNotEqualTo(reunionDTO2);
    }
}
