package com.myapp.repository;

import com.myapp.domain.PropertyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PropertyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {}
