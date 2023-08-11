package com.myapp.service.impl;

import com.myapp.domain.Service;
import com.myapp.repository.ServiceRepository;
import com.myapp.service.ServiceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Service}.
 */
@Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Service save(Service service) {
        log.debug("Request to save Service : {}", service);
        return serviceRepository.save(service);
    }

    @Override
    public Service update(Service service) {
        log.debug("Request to update Service : {}", service);
        return serviceRepository.save(service);
    }

    @Override
    public Optional<Service> partialUpdate(Service service) {
        log.debug("Request to partially update Service : {}", service);

        return serviceRepository
            .findById(service.getId())
            .map(existingService -> {
                if (service.getName() != null) {
                    existingService.setName(service.getName());
                }

                return existingService;
            })
            .map(serviceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> findAll() {
        log.debug("Request to get all Services");
        return serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> findOne(Long id) {
        log.debug("Request to get Service : {}", id);
        return serviceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Service : {}", id);
        serviceRepository.deleteById(id);
    }
}
