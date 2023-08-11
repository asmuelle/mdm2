package com.myapp.service.impl;

import com.myapp.domain.Namespace;
import com.myapp.repository.NamespaceRepository;
import com.myapp.service.NamespaceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Namespace}.
 */
@Service
@Transactional
public class NamespaceServiceImpl implements NamespaceService {

    private final Logger log = LoggerFactory.getLogger(NamespaceServiceImpl.class);

    private final NamespaceRepository namespaceRepository;

    public NamespaceServiceImpl(NamespaceRepository namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    @Override
    public Namespace save(Namespace namespace) {
        log.debug("Request to save Namespace : {}", namespace);
        return namespaceRepository.save(namespace);
    }

    @Override
    public Namespace update(Namespace namespace) {
        log.debug("Request to update Namespace : {}", namespace);
        return namespaceRepository.save(namespace);
    }

    @Override
    public Optional<Namespace> partialUpdate(Namespace namespace) {
        log.debug("Request to partially update Namespace : {}", namespace);

        return namespaceRepository
            .findById(namespace.getId())
            .map(existingNamespace -> {
                if (namespace.getName() != null) {
                    existingNamespace.setName(namespace.getName());
                }

                return existingNamespace;
            })
            .map(namespaceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Namespace> findAll() {
        log.debug("Request to get all Namespaces");
        return namespaceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Namespace> findOne(Long id) {
        log.debug("Request to get Namespace : {}", id);
        return namespaceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Namespace : {}", id);
        namespaceRepository.deleteById(id);
    }
}
