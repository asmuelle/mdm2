package com.myapp.repository;

import com.myapp.domain.Namespace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Namespace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {}
