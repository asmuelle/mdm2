package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WasteTrackingParametersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WasteTrackingParameters.class);
        WasteTrackingParameters wasteTrackingParameters1 = new WasteTrackingParameters();
        wasteTrackingParameters1.setId(1L);
        WasteTrackingParameters wasteTrackingParameters2 = new WasteTrackingParameters();
        wasteTrackingParameters2.setId(wasteTrackingParameters1.getId());
        assertThat(wasteTrackingParameters1).isEqualTo(wasteTrackingParameters2);
        wasteTrackingParameters2.setId(2L);
        assertThat(wasteTrackingParameters1).isNotEqualTo(wasteTrackingParameters2);
        wasteTrackingParameters1.setId(null);
        assertThat(wasteTrackingParameters1).isNotEqualTo(wasteTrackingParameters2);
    }
}
