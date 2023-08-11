package com.myapp.service.impl;

import com.myapp.domain.Scope;
import com.myapp.repository.ScopeRepository;
import com.myapp.service.ScopeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Scope}.
 */
@Service
@Transactional
public class ScopeServiceImpl implements ScopeService {

    private final Logger log = LoggerFactory.getLogger(ScopeServiceImpl.class);

    private final ScopeRepository scopeRepository;

    public ScopeServiceImpl(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public Scope save(Scope scope) {
        log.debug("Request to save Scope : {}", scope);
        return scopeRepository.save(scope);
    }

    @Override
    public Scope update(Scope scope) {
        log.debug("Request to update Scope : {}", scope);
        return scopeRepository.save(scope);
    }

    @Override
    public Optional<Scope> partialUpdate(Scope scope) {
        log.debug("Request to partially update Scope : {}", scope);

        return scopeRepository
            .findById(scope.getId())
            .map(existingScope -> {
                if (scope.getMeterDescription() != null) {
                    existingScope.setMeterDescription(scope.getMeterDescription());
                }
                if (scope.getMeterName() != null) {
                    existingScope.setMeterName(scope.getMeterName());
                }
                if (scope.getMeterUtility() != null) {
                    existingScope.setMeterUtility(scope.getMeterUtility());
                }
                if (scope.getPricePerMonth() != null) {
                    existingScope.setPricePerMonth(scope.getPricePerMonth());
                }

                return existingScope;
            })
            .map(scopeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scope> findAll() {
        log.debug("Request to get all Scopes");
        return scopeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Scope> findOne(Long id) {
        log.debug("Request to get Scope : {}", id);
        return scopeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scope : {}", id);
        scopeRepository.deleteById(id);
    }
}
