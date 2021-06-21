package com.meetingplanner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetingplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementSalleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementSalle.class);
        EquipementSalle equipementSalle1 = new EquipementSalle();
        equipementSalle1.setId(1L);
        EquipementSalle equipementSalle2 = new EquipementSalle();
        equipementSalle2.setId(equipementSalle1.getId());
        assertThat(equipementSalle1).isEqualTo(equipementSalle2);
        equipementSalle2.setId(2L);
        assertThat(equipementSalle1).isNotEqualTo(equipementSalle2);
        equipementSalle1.setId(null);
        assertThat(equipementSalle1).isNotEqualTo(equipementSalle2);
    }
}
