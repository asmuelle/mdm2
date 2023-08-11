package com.myapp.service;

import com.myapp.domain.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Service}.
 */
public interface ServiceService {
    /**
     * Save a service.
     *
     * @param service the entity to save.
     * @return the persisted entity.
     */
    Service save(Service service);

    /**
     * Updates a service.
     *
     * @param service the entity to update.
     * @return the persisted entity.
     */
    Service update(Service service);

    /**
     * Partially updates a service.
     *
     * @param service the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Service> partialUpdate(Service service);

    /**
     * Get all the services.
     *
     * @return the list of entities.
     */
    List<Service> findAll();

    /**
     * Get the "id" service.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Service> findOne(Long id);

    /**
     * Delete the "id" service.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
