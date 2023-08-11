package com.myapp.repository;

import com.myapp.domain.Meter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Meter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeterRepository extends JpaRepository<Meter, Long>, JpaSpecificationExecutor<Meter> {}
