package com.myapp.web.rest;

import com.myapp.domain.Namespace;
import com.myapp.repository.NamespaceRepository;
import com.myapp.service.NamespaceService;
import com.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.Namespace}.
 */
@RestController
@RequestMapping("/api")
public class NamespaceResource {

    private final Logger log = LoggerFactory.getLogger(NamespaceResource.class);

    private static final String ENTITY_NAME = "namespace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NamespaceService namespaceService;

    private final NamespaceRepository namespaceRepository;

    public NamespaceResource(NamespaceService namespaceService, NamespaceRepository namespaceRepository) {
        this.namespaceService = namespaceService;
        this.namespaceRepository = namespaceRepository;
    }

    /**
     * {@code POST  /namespaces} : Create a new namespace.
     *
     * @param namespace the namespace to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new namespace, or with status {@code 400 (Bad Request)} if the namespace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/namespaces")
    public ResponseEntity<Namespace> createNamespace(@Valid @RequestBody Namespace namespace) throws URISyntaxException {
        log.debug("REST request to save Namespace : {}", namespace);
        if (namespace.getId() != null) {
            throw new BadRequestAlertException("A new namespace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Namespace result = namespaceService.save(namespace);
        return ResponseEntity
            .created(new URI("/api/namespaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /namespaces/:id} : Updates an existing namespace.
     *
     * @param id the id of the namespace to save.
     * @param namespace the namespace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated namespace,
     * or with status {@code 400 (Bad Request)} if the namespace is not valid,
     * or with status {@code 500 (Internal Server Error)} if the namespace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/namespaces/{id}")
    public ResponseEntity<Namespace> updateNamespace(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Namespace namespace
    ) throws URISyntaxException {
        log.debug("REST request to update Namespace : {}, {}", id, namespace);
        if (namespace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, namespace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!namespaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Namespace result = namespaceService.update(namespace);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, namespace.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /namespaces/:id} : Partial updates given fields of an existing namespace, field will ignore if it is null
     *
     * @param id the id of the namespace to save.
     * @param namespace the namespace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated namespace,
     * or with status {@code 400 (Bad Request)} if the namespace is not valid,
     * or with status {@code 404 (Not Found)} if the namespace is not found,
     * or with status {@code 500 (Internal Server Error)} if the namespace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/namespaces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Namespace> partialUpdateNamespace(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Namespace namespace
    ) throws URISyntaxException {
        log.debug("REST request to partial update Namespace partially : {}, {}", id, namespace);
        if (namespace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, namespace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!namespaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Namespace> result = namespaceService.partialUpdate(namespace);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, namespace.getId().toString())
        );
    }

    /**
     * {@code GET  /namespaces} : get all the namespaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of namespaces in body.
     */
    @GetMapping("/namespaces")
    public List<Namespace> getAllNamespaces() {
        log.debug("REST request to get all Namespaces");
        return namespaceService.findAll();
    }

    /**
     * {@code GET  /namespaces/:id} : get the "id" namespace.
     *
     * @param id the id of the namespace to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the namespace, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/namespaces/{id}")
    public ResponseEntity<Namespace> getNamespace(@PathVariable Long id) {
        log.debug("REST request to get Namespace : {}", id);
        Optional<Namespace> namespace = namespaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(namespace);
    }

    /**
     * {@code DELETE  /namespaces/:id} : delete the "id" namespace.
     *
     * @param id the id of the namespace to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/namespaces/{id}")
    public ResponseEntity<Void> deleteNamespace(@PathVariable Long id) {
        log.debug("REST request to delete Namespace : {}", id);
        namespaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
