package com.myapp.repository;

import com.myapp.domain.OwnershipProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OwnershipProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnershipPropertyRepository extends JpaRepository<OwnershipProperty, Long> {}
