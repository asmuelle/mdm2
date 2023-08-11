package com.myapp.service;

import com.myapp.domain.PropertyType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PropertyType}.
 */
public interface PropertyTypeService {
    /**
     * Save a propertyType.
     *
     * @param propertyType the entity to save.
     * @return the persisted entity.
     */
    PropertyType save(PropertyType propertyType);

    /**
     * Updates a propertyType.
     *
     * @param propertyType the entity to update.
     * @return the persisted entity.
     */
    PropertyType update(PropertyType propertyType);

    /**
     * Partially updates a propertyType.
     *
     * @param propertyType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PropertyType> partialUpdate(PropertyType propertyType);

    /**
     * Get all the propertyTypes.
     *
     * @return the list of entities.
     */
    List<PropertyType> findAll();

    /**
     * Get the "id" propertyType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PropertyType> findOne(Long id);

    /**
     * Delete the "id" propertyType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
