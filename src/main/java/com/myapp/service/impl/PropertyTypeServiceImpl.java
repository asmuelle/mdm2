package com.myapp.service.impl;

import com.myapp.domain.PropertyType;
import com.myapp.repository.PropertyTypeRepository;
import com.myapp.service.PropertyTypeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PropertyType}.
 */
@Service
@Transactional
public class PropertyTypeServiceImpl implements PropertyTypeService {

    private final Logger log = LoggerFactory.getLogger(PropertyTypeServiceImpl.class);

    private final PropertyTypeRepository propertyTypeRepository;

    public PropertyTypeServiceImpl(PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
    }

    @Override
    public PropertyType save(PropertyType propertyType) {
        log.debug("Request to save PropertyType : {}", propertyType);
        return propertyTypeRepository.save(propertyType);
    }

    @Override
    public PropertyType update(PropertyType propertyType) {
        log.debug("Request to update PropertyType : {}", propertyType);
        return propertyTypeRepository.save(propertyType);
    }

    @Override
    public Optional<PropertyType> partialUpdate(PropertyType propertyType) {
        log.debug("Request to partially update PropertyType : {}", propertyType);

        return propertyTypeRepository
            .findById(propertyType.getId())
            .map(existingPropertyType -> {
                if (propertyType.getName() != null) {
                    existingPropertyType.setName(propertyType.getName());
                }
                if (propertyType.getPattern() != null) {
                    existingPropertyType.setPattern(propertyType.getPattern());
                }

                return existingPropertyType;
            })
            .map(propertyTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyType> findAll() {
        log.debug("Request to get all PropertyTypes");
        return propertyTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyType> findOne(Long id) {
        log.debug("Request to get PropertyType : {}", id);
        return propertyTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PropertyType : {}", id);
        propertyTypeRepository.deleteById(id);
    }
}
