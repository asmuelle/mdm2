package com.myapp.service;

import com.myapp.domain.Provider;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Provider}.
 */
public interface ProviderService {
    /**
     * Save a provider.
     *
     * @param provider the entity to save.
     * @return the persisted entity.
     */
    Provider save(Provider provider);

    /**
     * Updates a provider.
     *
     * @param provider the entity to update.
     * @return the persisted entity.
     */
    Provider update(Provider provider);

    /**
     * Partially updates a provider.
     *
     * @param provider the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Provider> partialUpdate(Provider provider);

    /**
     * Get all the providers.
     *
     * @return the list of entities.
     */
    List<Provider> findAll();

    /**
     * Get the "id" provider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Provider> findOne(Long id);

    /**
     * Delete the "id" provider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
