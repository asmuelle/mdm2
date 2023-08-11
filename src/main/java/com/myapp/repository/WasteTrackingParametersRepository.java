package com.myapp.repository;

import com.myapp.domain.WasteTrackingParameters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WasteTrackingParameters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WasteTrackingParametersRepository extends JpaRepository<WasteTrackingParameters, Long> {}
