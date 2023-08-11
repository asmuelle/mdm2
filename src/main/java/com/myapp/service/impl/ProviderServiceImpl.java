package com.myapp.service.impl;

import com.myapp.domain.Provider;
import com.myapp.repository.ProviderRepository;
import com.myapp.service.ProviderService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Provider}.
 */
@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

    private final Logger log = LoggerFactory.getLogger(ProviderServiceImpl.class);

    private final ProviderRepository providerRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public Provider save(Provider provider) {
        log.debug("Request to save Provider : {}", provider);
        return providerRepository.save(provider);
    }

    @Override
    public Provider update(Provider provider) {
        log.debug("Request to update Provider : {}", provider);
        return providerRepository.save(provider);
    }

    @Override
    public Optional<Provider> partialUpdate(Provider provider) {
        log.debug("Request to partially update Provider : {}", provider);

        return providerRepository
            .findById(provider.getId())
            .map(existingProvider -> {
                if (provider.getName() != null) {
                    existingProvider.setName(provider.getName());
                }

                return existingProvider;
            })
            .map(providerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Provider> findAll() {
        log.debug("Request to get all Providers");
        return providerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Provider> findOne(Long id) {
        log.debug("Request to get Provider : {}", id);
        return providerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Provider : {}", id);
        providerRepository.deleteById(id);
    }
}
