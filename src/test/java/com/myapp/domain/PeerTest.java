package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peer.class);
        Peer peer1 = new Peer();
        peer1.setId(1L);
        Peer peer2 = new Peer();
        peer2.setId(peer1.getId());
        assertThat(peer1).isEqualTo(peer2);
        peer2.setId(2L);
        assertThat(peer1).isNotEqualTo(peer2);
        peer1.setId(null);
        assertThat(peer1).isNotEqualTo(peer2);
    }
}
