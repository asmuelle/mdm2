package com.myapp.service;

import com.myapp.domain.Scope;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Scope}.
 */
public interface ScopeService {
    /**
     * Save a scope.
     *
     * @param scope the entity to save.
     * @return the persisted entity.
     */
    Scope save(Scope scope);

    /**
     * Updates a scope.
     *
     * @param scope the entity to update.
     * @return the persisted entity.
     */
    Scope update(Scope scope);

    /**
     * Partially updates a scope.
     *
     * @param scope the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Scope> partialUpdate(Scope scope);

    /**
     * Get all the scopes.
     *
     * @return the list of entities.
     */
    List<Scope> findAll();

    /**
     * Get the "id" scope.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Scope> findOne(Long id);

    /**
     * Delete the "id" scope.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
