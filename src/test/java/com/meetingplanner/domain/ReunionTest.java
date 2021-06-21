package com.meetingplanner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetingplanner.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReunionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reunion.class);
        Reunion reunion1 = new Reunion();
        reunion1.setId(1L);
        Reunion reunion2 = new Reunion();
        reunion2.setId(reunion1.getId());
        assertThat(reunion1).isEqualTo(reunion2);
        reunion2.setId(2L);
        assertThat(reunion1).isNotEqualTo(reunion2);
        reunion1.setId(null);
        assertThat(reunion1).isNotEqualTo(reunion2);
    }
}
