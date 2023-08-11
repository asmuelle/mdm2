package com.myapp.service.impl;

import com.myapp.domain.Owner;
import com.myapp.repository.OwnerRepository;
import com.myapp.service.OwnerService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Owner}.
 */
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner save(Owner owner) {
        log.debug("Request to save Owner : {}", owner);
        return ownerRepository.save(owner);
    }

    @Override
    public Owner update(Owner owner) {
        log.debug("Request to update Owner : {}", owner);
        return ownerRepository.save(owner);
    }

    @Override
    public Optional<Owner> partialUpdate(Owner owner) {
        log.debug("Request to partially update Owner : {}", owner);

        return ownerRepository
            .findById(owner.getId())
            .map(existingOwner -> {
                if (owner.getName() != null) {
                    existingOwner.setName(owner.getName());
                }
                if (owner.getFullName() != null) {
                    existingOwner.setFullName(owner.getFullName());
                }
                if (owner.getOwnerKey() != null) {
                    existingOwner.setOwnerKey(owner.getOwnerKey());
                }
                if (owner.getOwnerGroup() != null) {
                    existingOwner.setOwnerGroup(owner.getOwnerGroup());
                }
                if (owner.getMeters() != null) {
                    existingOwner.setMeters(owner.getMeters());
                }
                if (owner.getLastWeek() != null) {
                    existingOwner.setLastWeek(owner.getLastWeek());
                }
                if (owner.getBeforeLastWeek() != null) {
                    existingOwner.setBeforeLastWeek(owner.getBeforeLastWeek());
                }
                if (owner.getAmr() != null) {
                    existingOwner.setAmr(owner.getAmr());
                }
                if (owner.getLastYear() != null) {
                    existingOwner.setLastYear(owner.getLastYear());
                }
                if (owner.getContactEmail() != null) {
                    existingOwner.setContactEmail(owner.getContactEmail());
                }
                if (owner.getElectricityPrice() != null) {
                    existingOwner.setElectricityPrice(owner.getElectricityPrice());
                }
                if (owner.getGasPrice() != null) {
                    existingOwner.setGasPrice(owner.getGasPrice());
                }
                if (owner.getGasStage() != null) {
                    existingOwner.setGasStage(owner.getGasStage());
                }
                if (owner.getElectricityStage() != null) {
                    existingOwner.setElectricityStage(owner.getElectricityStage());
                }
                if (owner.getWaterStage() != null) {
                    existingOwner.setWaterStage(owner.getWaterStage());
                }
                if (owner.getHeatStage() != null) {
                    existingOwner.setHeatStage(owner.getHeatStage());
                }
                if (owner.getSolarHeat() != null) {
                    existingOwner.setSolarHeat(owner.getSolarHeat());
                }
                if (owner.getSolarPowerStage() != null) {
                    existingOwner.setSolarPowerStage(owner.getSolarPowerStage());
                }
                if (owner.getWindStage() != null) {
                    existingOwner.setWindStage(owner.getWindStage());
                }
                if (owner.getCogenPowerStage() != null) {
                    existingOwner.setCogenPowerStage(owner.getCogenPowerStage());
                }

                return existingOwner;
            })
            .map(ownerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Owner> findAll() {
        log.debug("Request to get all Owners");
        return ownerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Owner> findOne(Long id) {
        log.debug("Request to get Owner : {}", id);
        return ownerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
    }
}
