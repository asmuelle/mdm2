package com.myapp.service;

import com.myapp.domain.MeterImport;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link MeterImport}.
 */
public interface MeterImportService {
    /**
     * Save a meterImport.
     *
     * @param meterImport the entity to save.
     * @return the persisted entity.
     */
    MeterImport save(MeterImport meterImport);

    /**
     * Updates a meterImport.
     *
     * @param meterImport the entity to update.
     * @return the persisted entity.
     */
    MeterImport update(MeterImport meterImport);

    /**
     * Partially updates a meterImport.
     *
     * @param meterImport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MeterImport> partialUpdate(MeterImport meterImport);

    /**
     * Get all the meterImports.
     *
     * @return the list of entities.
     */
    List<MeterImport> findAll();

    /**
     * Get the "id" meterImport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MeterImport> findOne(Long id);

    /**
     * Delete the "id" meterImport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
