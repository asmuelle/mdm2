package com.myapp.service.impl;

import com.myapp.domain.Meter;
import com.myapp.repository.MeterRepository;
import com.myapp.service.MeterService;
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
 * Service Implementation for managing {@link Meter}.
 */
@Service
@Transactional
public class MeterServiceImpl implements MeterService {

    private final Logger log = LoggerFactory.getLogger(MeterServiceImpl.class);

    private final MeterRepository meterRepository;

    public MeterServiceImpl(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    @Override
    public Meter save(Meter meter) {
        log.debug("Request to save Meter : {}", meter);
        return meterRepository.save(meter);
    }

    @Override
    public Meter update(Meter meter) {
        log.debug("Request to update Meter : {}", meter);
        return meterRepository.save(meter);
    }

    @Override
    public Optional<Meter> partialUpdate(Meter meter) {
        log.debug("Request to partially update Meter : {}", meter);

        return meterRepository
            .findById(meter.getId())
            .map(existingMeter -> {
                if (meter.getName() != null) {
                    existingMeter.setName(meter.getName());
                }
                if (meter.getAmrWeek() != null) {
                    existingMeter.setAmrWeek(meter.getAmrWeek());
                }
                if (meter.getAmrYear() != null) {
                    existingMeter.setAmrYear(meter.getAmrYear());
                }
                if (meter.getUtility() != null) {
                    existingMeter.setUtility(meter.getUtility());
                }
                if (meter.getLoadType() != null) {
                    existingMeter.setLoadType(meter.getLoadType());
                }
                if (meter.getPrice() != null) {
                    existingMeter.setPrice(meter.getPrice());
                }
                if (meter.getLastReading() != null) {
                    existingMeter.setLastReading(meter.getLastReading());
                }
                if (meter.getContactEmail() != null) {
                    existingMeter.setContactEmail(meter.getContactEmail());
                }

                return existingMeter;
            })
            .map(meterRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Meter> findAll(Pageable pageable) {
        log.debug("Request to get all Meters");
        return meterRepository.findAll(pageable);
    }

    /**
     *  Get all the meters where Meter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Meter> findAllWhereMeterIsNull() {
        log.debug("Request to get all meters where Meter is null");
        return StreamSupport.stream(meterRepository.findAll().spliterator(), false).filter(meter -> meter.getMeter() == null).toList();
    }

    /**
     *  Get all the meters where Meter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Meter> findAllWhereMeterIsNull() {
        log.debug("Request to get all meters where Meter is null");
        return StreamSupport.stream(meterRepository.findAll().spliterator(), false).filter(meter -> meter.getMeter() == null).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Meter> findOne(Long id) {
        log.debug("Request to get Meter : {}", id);
        return meterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Meter : {}", id);
        meterRepository.deleteById(id);
    }
}
