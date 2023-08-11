package com.myapp.service;

import com.myapp.domain.WasteTrackingParameters;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WasteTrackingParameters}.
 */
public interface WasteTrackingParametersService {
    /**
     * Save a wasteTrackingParameters.
     *
     * @param wasteTrackingParameters the entity to save.
     * @return the persisted entity.
     */
    WasteTrackingParameters save(WasteTrackingParameters wasteTrackingParameters);

    /**
     * Updates a wasteTrackingParameters.
     *
     * @param wasteTrackingParameters the entity to update.
     * @return the persisted entity.
     */
    WasteTrackingParameters update(WasteTrackingParameters wasteTrackingParameters);

    /**
     * Partially updates a wasteTrackingParameters.
     *
     * @param wasteTrackingParameters the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WasteTrackingParameters> partialUpdate(WasteTrackingParameters wasteTrackingParameters);

    /**
     * Get all the wasteTrackingParameters.
     *
     * @return the list of entities.
     */
    List<WasteTrackingParameters> findAll();

    /**
     * Get the "id" wasteTrackingParameters.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WasteTrackingParameters> findOne(Long id);

    /**
     * Delete the "id" wasteTrackingParameters.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
