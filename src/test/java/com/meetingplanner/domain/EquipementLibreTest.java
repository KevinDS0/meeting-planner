package com.meetingplanner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetingplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementLibreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementLibre.class);
        EquipementLibre equipementLibre1 = new EquipementLibre();
        equipementLibre1.setId(1L);
        EquipementLibre equipementLibre2 = new EquipementLibre();
        equipementLibre2.setId(equipementLibre1.getId());
        assertThat(equipementLibre1).isEqualTo(equipementLibre2);
        equipementLibre2.setId(2L);
        assertThat(equipementLibre1).isNotEqualTo(equipementLibre2);
        equipementLibre1.setId(null);
        assertThat(equipementLibre1).isNotEqualTo(equipementLibre2);
    }
}
