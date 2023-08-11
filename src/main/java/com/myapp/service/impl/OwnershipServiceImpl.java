package com.myapp.service.impl;

import com.myapp.domain.Ownership;
import com.myapp.repository.OwnershipRepository;
import com.myapp.service.OwnershipService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ownership}.
 */
@Service
@Transactional
public class OwnershipServiceImpl implements OwnershipService {

    private final Logger log = LoggerFactory.getLogger(OwnershipServiceImpl.class);

    private final OwnershipRepository ownershipRepository;

    public OwnershipServiceImpl(OwnershipRepository ownershipRepository) {
        this.ownershipRepository = ownershipRepository;
    }

    @Override
    public Ownership save(Ownership ownership) {
        log.debug("Request to save Ownership : {}", ownership);
        return ownershipRepository.save(ownership);
    }

    @Override
    public Ownership update(Ownership ownership) {
        log.debug("Request to update Ownership : {}", ownership);
        return ownershipRepository.save(ownership);
    }

    @Override
    public Optional<Ownership> partialUpdate(Ownership ownership) {
        log.debug("Request to partially update Ownership : {}", ownership);

        return ownershipRepository
            .findById(ownership.getId())
            .map(existingOwnership -> {
                if (ownership.getName() != null) {
                    existingOwnership.setName(ownership.getName());
                }
                if (ownership.getClientRef() != null) {
                    existingOwnership.setClientRef(ownership.getClientRef());
                }
                if (ownership.getStartDate() != null) {
                    existingOwnership.setStartDate(ownership.getStartDate());
                }
                if (ownership.getEndDate() != null) {
                    existingOwnership.setEndDate(ownership.getEndDate());
                }

                return existingOwnership;
            })
            .map(ownershipRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ownership> findAll(Pageable pageable) {
        log.debug("Request to get all Ownerships");
        return ownershipRepository.findAll(pageable);
    }

    public Page<Ownership> findAllWithEagerRelationships(Pageable pageable) {
        return ownershipRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the ownerships where Address is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Ownership> findAllWhereAddressIsNull() {
        log.debug("Request to get all ownerships where Address is null");
        return StreamSupport
            .stream(ownershipRepository.findAll().spliterator(), false)
            .filter(ownership -> ownership.getAddress() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ownership> findOne(Long id) {
        log.debug("Request to get Ownership : {}", id);
        return ownershipRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ownership : {}", id);
        ownershipRepository.deleteById(id);
    }
}
