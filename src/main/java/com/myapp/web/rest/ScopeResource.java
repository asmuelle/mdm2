package com.myapp.web.rest;

import com.myapp.domain.Scope;
import com.myapp.repository.ScopeRepository;
import com.myapp.service.ScopeService;
import com.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.myapp.domain.Scope}.
 */
@RestController
@RequestMapping("/api")
public class ScopeResource {

    private final Logger log = LoggerFactory.getLogger(ScopeResource.class);

    private static final String ENTITY_NAME = "scope";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScopeService scopeService;

    private final ScopeRepository scopeRepository;

    public ScopeResource(ScopeService scopeService, ScopeRepository scopeRepository) {
        this.scopeService = scopeService;
        this.scopeRepository = scopeRepository;
    }

    /**
     * {@code POST  /scopes} : Create a new scope.
     *
     * @param scope the scope to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scope, or with status {@code 400 (Bad Request)} if the scope has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scopes")
    public ResponseEntity<Scope> createScope(@RequestBody Scope scope) throws URISyntaxException {
        log.debug("REST request to save Scope : {}", scope);
        if (scope.getId() != null) {
            throw new BadRequestAlertException("A new scope cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scope result = scopeService.save(scope);
        return ResponseEntity
            .created(new URI("/api/scopes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scopes/:id} : Updates an existing scope.
     *
     * @param id the id of the scope to save.
     * @param scope the scope to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scope,
     * or with status {@code 400 (Bad Request)} if the scope is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scope couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scopes/{id}")
    public ResponseEntity<Scope> updateScope(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scope scope)
        throws URISyntaxException {
        log.debug("REST request to update Scope : {}, {}", id, scope);
        if (scope.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scope.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scopeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Scope result = scopeService.update(scope);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scope.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scopes/:id} : Partial updates given fields of an existing scope, field will ignore if it is null
     *
     * @param id the id of the scope to save.
     * @param scope the scope to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scope,
     * or with status {@code 400 (Bad Request)} if the scope is not valid,
     * or with status {@code 404 (Not Found)} if the scope is not found,
     * or with status {@code 500 (Internal Server Error)} if the scope couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scopes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Scope> partialUpdateScope(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scope scope)
        throws URISyntaxException {
        log.debug("REST request to partial update Scope partially : {}, {}", id, scope);
        if (scope.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scope.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scopeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Scope> result = scopeService.partialUpdate(scope);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scope.getId().toString())
        );
    }

    /**
     * {@code GET  /scopes} : get all the scopes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scopes in body.
     */
    @GetMapping("/scopes")
    public List<Scope> getAllScopes() {
        log.debug("REST request to get all Scopes");
        return scopeService.findAll();
    }

    /**
     * {@code GET  /scopes/:id} : get the "id" scope.
     *
     * @param id the id of the scope to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scope, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scopes/{id}")
    public ResponseEntity<Scope> getScope(@PathVariable Long id) {
        log.debug("REST request to get Scope : {}", id);
        Optional<Scope> scope = scopeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scope);
    }

    /**
     * {@code DELETE  /scopes/:id} : delete the "id" scope.
     *
     * @param id the id of the scope to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scopes/{id}")
    public ResponseEntity<Void> deleteScope(@PathVariable Long id) {
        log.debug("REST request to delete Scope : {}", id);
        scopeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
