package com.myapp.service.impl;

import com.myapp.domain.OwnershipClassification;
import com.myapp.repository.OwnershipClassificationRepository;
import com.myapp.service.OwnershipClassificationService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OwnershipClassification}.
 */
@Service
@Transactional
public class OwnershipClassificationServiceImpl implements OwnershipClassificationService {

    private final Logger log = LoggerFactory.getLogger(OwnershipClassificationServiceImpl.class);

    private final OwnershipClassificationRepository ownershipClassificationRepository;

    public OwnershipClassificationServiceImpl(OwnershipClassificationRepository ownershipClassificationRepository) {
        this.ownershipClassificationRepository = ownershipClassificationRepository;
    }

    @Override
    public OwnershipClassification save(OwnershipClassification ownershipClassification) {
        log.debug("Request to save OwnershipClassification : {}", ownershipClassification);
        return ownershipClassificationRepository.save(ownershipClassification);
    }

    @Override
    public OwnershipClassification update(OwnershipClassification ownershipClassification) {
        log.debug("Request to update OwnershipClassification : {}", ownershipClassification);
        return ownershipClassificationRepository.save(ownershipClassification);
    }

    @Override
    public Optional<OwnershipClassification> partialUpdate(OwnershipClassification ownershipClassification) {
        log.debug("Request to partially update OwnershipClassification : {}", ownershipClassification);

        return ownershipClassificationRepository
            .findById(ownershipClassification.getId())
            .map(existingOwnershipClassification -> {
                if (ownershipClassification.getName() != null) {
                    existingOwnershipClassification.setName(ownershipClassification.getName());
                }

                return existingOwnershipClassification;
            })
            .map(ownershipClassificationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnershipClassification> findAll() {
        log.debug("Request to get all OwnershipClassifications");
        return ownershipClassificationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnershipClassification> findOne(Long id) {
        log.debug("Request to get OwnershipClassification : {}", id);
        return ownershipClassificationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OwnershipClassification : {}", id);
        ownershipClassificationRepository.deleteById(id);
    }
}
