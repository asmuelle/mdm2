package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PropertyTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyType.class);
        PropertyType propertyType1 = new PropertyType();
        propertyType1.setId(1L);
        PropertyType propertyType2 = new PropertyType();
        propertyType2.setId(propertyType1.getId());
        assertThat(propertyType1).isEqualTo(propertyType2);
        propertyType2.setId(2L);
        assertThat(propertyType1).isNotEqualTo(propertyType2);
        propertyType1.setId(null);
        assertThat(propertyType1).isNotEqualTo(propertyType2);
    }
}
