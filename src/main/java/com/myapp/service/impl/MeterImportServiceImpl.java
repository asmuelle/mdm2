package com.myapp.service.impl;

import com.myapp.domain.MeterImport;
import com.myapp.repository.MeterImportRepository;
import com.myapp.service.MeterImportService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MeterImport}.
 */
@Service
@Transactional
public class MeterImportServiceImpl implements MeterImportService {

    private final Logger log = LoggerFactory.getLogger(MeterImportServiceImpl.class);

    private final MeterImportRepository meterImportRepository;

    public MeterImportServiceImpl(MeterImportRepository meterImportRepository) {
        this.meterImportRepository = meterImportRepository;
    }

    @Override
    public MeterImport save(MeterImport meterImport) {
        log.debug("Request to save MeterImport : {}", meterImport);
        return meterImportRepository.save(meterImport);
    }

    @Override
    public MeterImport update(MeterImport meterImport) {
        log.debug("Request to update MeterImport : {}", meterImport);
        return meterImportRepository.save(meterImport);
    }

    @Override
    public Optional<MeterImport> partialUpdate(MeterImport meterImport) {
        log.debug("Request to partially update MeterImport : {}", meterImport);

        return meterImportRepository
            .findById(meterImport.getId())
            .map(existingMeterImport -> {
                if (meterImport.getProvider() != null) {
                    existingMeterImport.setProvider(meterImport.getProvider());
                }
                if (meterImport.getUtility() != null) {
                    existingMeterImport.setUtility(meterImport.getUtility());
                }
                if (meterImport.getNamespace() != null) {
                    existingMeterImport.setNamespace(meterImport.getNamespace());
                }
                if (meterImport.getClientRef() != null) {
                    existingMeterImport.setClientRef(meterImport.getClientRef());
                }
                if (meterImport.getMeterName() != null) {
                    existingMeterImport.setMeterName(meterImport.getMeterName());
                }
                if (meterImport.getContactEmail() != null) {
                    existingMeterImport.setContactEmail(meterImport.getContactEmail());
                }
                if (meterImport.getOwnership() != null) {
                    existingMeterImport.setOwnership(meterImport.getOwnership());
                }
                if (meterImport.getOwner() != null) {
                    existingMeterImport.setOwner(meterImport.getOwner());
                }
                if (meterImport.getPostcode() != null) {
                    existingMeterImport.setPostcode(meterImport.getPostcode());
                }
                if (meterImport.getAddresslines() != null) {
                    existingMeterImport.setAddresslines(meterImport.getAddresslines());
                }
                if (meterImport.getLat() != null) {
                    existingMeterImport.setLat(meterImport.getLat());
                }
                if (meterImport.getLon() != null) {
                    existingMeterImport.setLon(meterImport.getLon());
                }
                if (meterImport.getClassifications() != null) {
                    existingMeterImport.setClassifications(meterImport.getClassifications());
                }

                return existingMeterImport;
            })
            .map(meterImportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeterImport> findAll() {
        log.debug("Request to get all MeterImports");
        return meterImportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeterImport> findOne(Long id) {
        log.debug("Request to get MeterImport : {}", id);
        return meterImportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MeterImport : {}", id);
        meterImportRepository.deleteById(id);
    }
}
