package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OwnershipClassificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OwnershipClassification.class);
        OwnershipClassification ownershipClassification1 = new OwnershipClassification();
        ownershipClassification1.setId(1L);
        OwnershipClassification ownershipClassification2 = new OwnershipClassification();
        ownershipClassification2.setId(ownershipClassification1.getId());
        assertThat(ownershipClassification1).isEqualTo(ownershipClassification2);
        ownershipClassification2.setId(2L);
        assertThat(ownershipClassification1).isNotEqualTo(ownershipClassification2);
        ownershipClassification1.setId(null);
        assertThat(ownershipClassification1).isNotEqualTo(ownershipClassification2);
    }
}
