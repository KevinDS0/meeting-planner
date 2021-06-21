package com.meetingplanner.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetingplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementLibreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementLibreDTO.class);
        EquipementLibreDTO equipementLibreDTO1 = new EquipementLibreDTO();
        equipementLibreDTO1.setId(1L);
        EquipementLibreDTO equipementLibreDTO2 = new EquipementLibreDTO();
        assertThat(equipementLibreDTO1).isNotEqualTo(equipementLibreDTO2);
        equipementLibreDTO2.setId(equipementLibreDTO1.getId());
        assertThat(equipementLibreDTO1).isEqualTo(equipementLibreDTO2);
        equipementLibreDTO2.setId(2L);
        assertThat(equipementLibreDTO1).isNotEqualTo(equipementLibreDTO2);
        equipementLibreDTO1.setId(null);
        assertThat(equipementLibreDTO1).isNotEqualTo(equipementLibreDTO2);
    }
}
