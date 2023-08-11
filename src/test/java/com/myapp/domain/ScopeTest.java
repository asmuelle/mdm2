package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScopeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scope.class);
        Scope scope1 = new Scope();
        scope1.setId(1L);
        Scope scope2 = new Scope();
        scope2.setId(scope1.getId());
        assertThat(scope1).isEqualTo(scope2);
        scope2.setId(2L);
        assertThat(scope1).isNotEqualTo(scope2);
        scope1.setId(null);
        assertThat(scope1).isNotEqualTo(scope2);
    }
}
