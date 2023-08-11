package com.myapp.service.impl;

import com.myapp.domain.OwnershipProperty;
import com.myapp.repository.OwnershipPropertyRepository;
import com.myapp.service.OwnershipPropertyService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OwnershipProperty}.
 */
@Service
@Transactional
public class OwnershipPropertyServiceImpl implements OwnershipPropertyService {

    private final Logger log = LoggerFactory.getLogger(OwnershipPropertyServiceImpl.class);

    private final OwnershipPropertyRepository ownershipPropertyRepository;

    public OwnershipPropertyServiceImpl(OwnershipPropertyRepository ownershipPropertyRepository) {
        this.ownershipPropertyRepository = ownershipPropertyRepository;
    }

    @Override
    public OwnershipProperty save(OwnershipProperty ownershipProperty) {
        log.debug("Request to save OwnershipProperty : {}", ownershipProperty);
        return ownershipPropertyRepository.save(ownershipProperty);
    }

    @Override
    public OwnershipProperty update(OwnershipProperty ownershipProperty) {
        log.debug("Request to update OwnershipProperty : {}", ownershipProperty);
        return ownershipPropertyRepository.save(ownershipProperty);
    }

    @Override
    public Optional<OwnershipProperty> partialUpdate(OwnershipProperty ownershipProperty) {
        log.debug("Request to partially update OwnershipProperty : {}", ownershipProperty);

        return ownershipPropertyRepository
            .findById(ownershipProperty.getId())
            .map(existingOwnershipProperty -> {
                if (ownershipProperty.getValue() != null) {
                    existingOwnershipProperty.setValue(ownershipProperty.getValue());
                }

                return existingOwnershipProperty;
            })
            .map(ownershipPropertyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnershipProperty> findAll() {
        log.debug("Request to get all OwnershipProperties");
        return ownershipPropertyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnershipProperty> findOne(Long id) {
        log.debug("Request to get OwnershipProperty : {}", id);
        return ownershipPropertyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OwnershipProperty : {}", id);
        ownershipPropertyRepository.deleteById(id);
    }
}
