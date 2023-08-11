package com.myapp.service;

import com.myapp.domain.Ownership;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ownership}.
 */
public interface OwnershipService {
    /**
     * Save a ownership.
     *
     * @param ownership the entity to save.
     * @return the persisted entity.
     */
    Ownership save(Ownership ownership);

    /**
     * Updates a ownership.
     *
     * @param ownership the entity to update.
     * @return the persisted entity.
     */
    Ownership update(Ownership ownership);

    /**
     * Partially updates a ownership.
     *
     * @param ownership the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ownership> partialUpdate(Ownership ownership);

    /**
     * Get all the ownerships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ownership> findAll(Pageable pageable);

    /**
     * Get all the Ownership where Address is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Ownership> findAllWhereAddressIsNull();

    /**
     * Get all the ownerships with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ownership> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" ownership.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ownership> findOne(Long id);

    /**
     * Delete the "id" ownership.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
