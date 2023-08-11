package com.myapp.repository;

import com.myapp.domain.MeterImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MeterImport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeterImportRepository extends JpaRepository<MeterImport, Long> {}
