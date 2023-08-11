package com.myapp.repository;

import com.myapp.domain.OwnershipClassification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OwnershipClassification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnershipClassificationRepository
    extends JpaRepository<OwnershipClassification, Long>, JpaSpecificationExecutor<OwnershipClassification> {}
