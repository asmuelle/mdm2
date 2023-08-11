package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OwnershipPropertyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OwnershipProperty.class);
        OwnershipProperty ownershipProperty1 = new OwnershipProperty();
        ownershipProperty1.setId(1L);
        OwnershipProperty ownershipProperty2 = new OwnershipProperty();
        ownershipProperty2.setId(ownershipProperty1.getId());
        assertThat(ownershipProperty1).isEqualTo(ownershipProperty2);
        ownershipProperty2.setId(2L);
        assertThat(ownershipProperty1).isNotEqualTo(ownershipProperty2);
        ownershipProperty1.setId(null);
        assertThat(ownershipProperty1).isNotEqualTo(ownershipProperty2);
    }
}
