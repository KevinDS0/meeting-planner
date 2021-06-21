package com.meetingplanner.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetingplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementSalleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementSalleDTO.class);
        EquipementSalleDTO equipementSalleDTO1 = new EquipementSalleDTO();
        equipementSalleDTO1.setId(1L);
        EquipementSalleDTO equipementSalleDTO2 = new EquipementSalleDTO();
        assertThat(equipementSalleDTO1).isNotEqualTo(equipementSalleDTO2);
        equipementSalleDTO2.setId(equipementSalleDTO1.getId());
        assertThat(equipementSalleDTO1).isEqualTo(equipementSalleDTO2);
        equipementSalleDTO2.setId(2L);
        assertThat(equipementSalleDTO1).isNotEqualTo(equipementSalleDTO2);
        equipementSalleDTO1.setId(null);
        assertThat(equipementSalleDTO1).isNotEqualTo(equipementSalleDTO2);
    }
}
