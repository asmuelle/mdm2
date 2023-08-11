package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meter.class);
        Meter meter1 = new Meter();
        meter1.setId(1L);
        Meter meter2 = new Meter();
        meter2.setId(meter1.getId());
        assertThat(meter1).isEqualTo(meter2);
        meter2.setId(2L);
        assertThat(meter1).isNotEqualTo(meter2);
        meter1.setId(null);
        assertThat(meter1).isNotEqualTo(meter2);
    }
}
