package com.myapp.service;

import com.myapp.domain.Meter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Meter}.
 */
public interface MeterService {
    /**
     * Save a meter.
     *
     * @param meter the entity to save.
     * @return the persisted entity.
     */
    Meter save(Meter meter);

    /**
     * Updates a meter.
     *
     * @param meter the entity to update.
     * @return the persisted entity.
     */
    Meter update(Meter meter);

    /**
     * Partially updates a meter.
     *
     * @param meter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Meter> partialUpdate(Meter meter);

    /**
     * Get all the meters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Meter> findAll(Pageable pageable);

    /**
     * Get all the Meter where Meter is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Meter> findAllWhereMeterIsNull();
    /**
     * Get all the Meter where Meter is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Meter> findAllWhereMeterIsNull();

    /**
     * Get the "id" meter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Meter> findOne(Long id);

    /**
     * Delete the "id" meter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
