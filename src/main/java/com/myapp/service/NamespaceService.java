package com.myapp.service;

import com.myapp.domain.Namespace;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Namespace}.
 */
public interface NamespaceService {
    /**
     * Save a namespace.
     *
     * @param namespace the entity to save.
     * @return the persisted entity.
     */
    Namespace save(Namespace namespace);

    /**
     * Updates a namespace.
     *
     * @param namespace the entity to update.
     * @return the persisted entity.
     */
    Namespace update(Namespace namespace);

    /**
     * Partially updates a namespace.
     *
     * @param namespace the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Namespace> partialUpdate(Namespace namespace);

    /**
     * Get all the namespaces.
     *
     * @return the list of entities.
     */
    List<Namespace> findAll();

    /**
     * Get the "id" namespace.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Namespace> findOne(Long id);

    /**
     * Delete the "id" namespace.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
