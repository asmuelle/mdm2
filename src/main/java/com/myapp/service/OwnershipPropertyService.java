package com.myapp.service;

import com.myapp.domain.OwnershipProperty;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OwnershipProperty}.
 */
public interface OwnershipPropertyService {
    /**
     * Save a ownershipProperty.
     *
     * @param ownershipProperty the entity to save.
     * @return the persisted entity.
     */
    OwnershipProperty save(OwnershipProperty ownershipProperty);

    /**
     * Updates a ownershipProperty.
     *
     * @param ownershipProperty the entity to update.
     * @return the persisted entity.
     */
    OwnershipProperty update(OwnershipProperty ownershipProperty);

    /**
     * Partially updates a ownershipProperty.
     *
     * @param ownershipProperty the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnershipProperty> partialUpdate(OwnershipProperty ownershipProperty);

    /**
     * Get all the ownershipProperties.
     *
     * @return the list of entities.
     */
    List<OwnershipProperty> findAll();

    /**
     * Get the "id" ownershipProperty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnershipProperty> findOne(Long id);

    /**
     * Delete the "id" ownershipProperty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
