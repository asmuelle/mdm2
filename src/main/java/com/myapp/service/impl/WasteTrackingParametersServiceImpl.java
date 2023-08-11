package com.myapp.service.impl;

import com.myapp.domain.WasteTrackingParameters;
import com.myapp.repository.WasteTrackingParametersRepository;
import com.myapp.service.WasteTrackingParametersService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WasteTrackingParameters}.
 */
@Service
@Transactional
public class WasteTrackingParametersServiceImpl implements WasteTrackingParametersService {

    private final Logger log = LoggerFactory.getLogger(WasteTrackingParametersServiceImpl.class);

    private final WasteTrackingParametersRepository wasteTrackingParametersRepository;

    public WasteTrackingParametersServiceImpl(WasteTrackingParametersRepository wasteTrackingParametersRepository) {
        this.wasteTrackingParametersRepository = wasteTrackingParametersRepository;
    }

    @Override
    public WasteTrackingParameters save(WasteTrackingParameters wasteTrackingParameters) {
        log.debug("Request to save WasteTrackingParameters : {}", wasteTrackingParameters);
        return wasteTrackingParametersRepository.save(wasteTrackingParameters);
    }

    @Override
    public WasteTrackingParameters update(WasteTrackingParameters wasteTrackingParameters) {
        log.debug("Request to update WasteTrackingParameters : {}", wasteTrackingParameters);
        return wasteTrackingParametersRepository.save(wasteTrackingParameters);
    }

    @Override
    public Optional<WasteTrackingParameters> partialUpdate(WasteTrackingParameters wasteTrackingParameters) {
        log.debug("Request to partially update WasteTrackingParameters : {}", wasteTrackingParameters);

        return wasteTrackingParametersRepository
            .findById(wasteTrackingParameters.getId())
            .map(existingWasteTrackingParameters -> {
                if (wasteTrackingParameters.getName() != null) {
                    existingWasteTrackingParameters.setName(wasteTrackingParameters.getName());
                }
                if (wasteTrackingParameters.getWasteIssueCreationThreshold() != null) {
                    existingWasteTrackingParameters.setWasteIssueCreationThreshold(
                        wasteTrackingParameters.getWasteIssueCreationThreshold()
                    );
                }
                if (wasteTrackingParameters.getMaxWasteIssueCreationRate() != null) {
                    existingWasteTrackingParameters.setMaxWasteIssueCreationRate(wasteTrackingParameters.getMaxWasteIssueCreationRate());
                }
                if (wasteTrackingParameters.getMaxActiveWasteIssues() != null) {
                    existingWasteTrackingParameters.setMaxActiveWasteIssues(wasteTrackingParameters.getMaxActiveWasteIssues());
                }
                if (wasteTrackingParameters.getAutoCreateWasteIssues() != null) {
                    existingWasteTrackingParameters.setAutoCreateWasteIssues(wasteTrackingParameters.getAutoCreateWasteIssues());
                }

                return existingWasteTrackingParameters;
            })
            .map(wasteTrackingParametersRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WasteTrackingParameters> findAll() {
        log.debug("Request to get all WasteTrackingParameters");
        return wasteTrackingParametersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WasteTrackingParameters> findOne(Long id) {
        log.debug("Request to get WasteTrackingParameters : {}", id);
        return wasteTrackingParametersRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WasteTrackingParameters : {}", id);
        wasteTrackingParametersRepository.deleteById(id);
    }
}
