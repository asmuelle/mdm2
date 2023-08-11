package com.myapp.repository;

import com.myapp.domain.Peer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Peer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeerRepository extends JpaRepository<Peer, Long>, JpaSpecificationExecutor<Peer> {}
