package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeterImportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeterImport.class);
        MeterImport meterImport1 = new MeterImport();
        meterImport1.setId(1L);
        MeterImport meterImport2 = new MeterImport();
        meterImport2.setId(meterImport1.getId());
        assertThat(meterImport1).isEqualTo(meterImport2);
        meterImport2.setId(2L);
        assertThat(meterImport1).isNotEqualTo(meterImport2);
        meterImport1.setId(null);
        assertThat(meterImport1).isNotEqualTo(meterImport2);
    }
}
