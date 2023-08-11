package com.myapp.service;

import com.myapp.domain.OwnershipClassification;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OwnershipClassification}.
 */
public interface OwnershipClassificationService {
    /**
     * Save a ownershipClassification.
     *
     * @param ownershipClassification the entity to save.
     * @return the persisted entity.
     */
    OwnershipClassification save(OwnershipClassification ownershipClassification);

    /**
     * Updates a ownershipClassification.
     *
     * @param ownershipClassification the entity to update.
     * @return the persisted entity.
     */
    OwnershipClassification update(OwnershipClassification ownershipClassification);

    /**
     * Partially updates a ownershipClassification.
     *
     * @param ownershipClassification the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnershipClassification> partialUpdate(OwnershipClassification ownershipClassification);

    /**
     * Get all the ownershipClassifications.
     *
     * @return the list of entities.
     */
    List<OwnershipClassification> findAll();

    /**
     * Get the "id" ownershipClassification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnershipClassification> findOne(Long id);

    /**
     * Delete the "id" ownershipClassification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
