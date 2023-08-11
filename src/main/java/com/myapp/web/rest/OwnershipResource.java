package com.myapp.web.rest;

import com.myapp.domain.Ownership;
import com.myapp.repository.OwnershipRepository;
import com.myapp.service.OwnershipQueryService;
import com.myapp.service.OwnershipService;
import com.myapp.service.criteria.OwnershipCriteria;
import com.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.Ownership}.
 */
@RestController
@RequestMapping("/api")
public class OwnershipResource {

    private final Logger log = LoggerFactory.getLogger(OwnershipResource.class);

    private static final String ENTITY_NAME = "ownership";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnershipService ownershipService;

    private final OwnershipRepository ownershipRepository;

    private final OwnershipQueryService ownershipQueryService;

    public OwnershipResource(
        OwnershipService ownershipService,
        OwnershipRepository ownershipRepository,
        OwnershipQueryService ownershipQueryService
    ) {
        this.ownershipService = ownershipService;
        this.ownershipRepository = ownershipRepository;
        this.ownershipQueryService = ownershipQueryService;
    }

    /**
     * {@code POST  /ownerships} : Create a new ownership.
     *
     * @param ownership the ownership to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ownership, or with status {@code 400 (Bad Request)} if the ownership has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ownerships")
    public ResponseEntity<Ownership> createOwnership(@Valid @RequestBody Ownership ownership) throws URISyntaxException {
        log.debug("REST request to save Ownership : {}", ownership);
        if (ownership.getId() != null) {
            throw new BadRequestAlertException("A new ownership cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ownership result = ownershipService.save(ownership);
        return ResponseEntity
            .created(new URI("/api/ownerships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ownerships/:id} : Updates an existing ownership.
     *
     * @param id the id of the ownership to save.
     * @param ownership the ownership to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownership,
     * or with status {@code 400 (Bad Request)} if the ownership is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ownership couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ownerships/{id}")
    public ResponseEntity<Ownership> updateOwnership(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ownership ownership
    ) throws URISyntaxException {
        log.debug("REST request to update Ownership : {}, {}", id, ownership);
        if (ownership.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownership.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ownership result = ownershipService.update(ownership);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownership.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ownerships/:id} : Partial updates given fields of an existing ownership, field will ignore if it is null
     *
     * @param id the id of the ownership to save.
     * @param ownership the ownership to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownership,
     * or with status {@code 400 (Bad Request)} if the ownership is not valid,
     * or with status {@code 404 (Not Found)} if the ownership is not found,
     * or with status {@code 500 (Internal Server Error)} if the ownership couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ownerships/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ownership> partialUpdateOwnership(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ownership ownership
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ownership partially : {}, {}", id, ownership);
        if (ownership.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownership.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ownership> result = ownershipService.partialUpdate(ownership);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownership.getId().toString())
        );
    }

    /**
     * {@code GET  /ownerships} : get all the ownerships.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ownerships in body.
     */
    @GetMapping("/ownerships")
    public ResponseEntity<List<Ownership>> getAllOwnerships(
        OwnershipCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Ownerships by criteria: {}", criteria);

        Page<Ownership> page = ownershipQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ownerships/count} : count all the ownerships.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ownerships/count")
    public ResponseEntity<Long> countOwnerships(OwnershipCriteria criteria) {
        log.debug("REST request to count Ownerships by criteria: {}", criteria);
        return ResponseEntity.ok().body(ownershipQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ownerships/:id} : get the "id" ownership.
     *
     * @param id the id of the ownership to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ownership, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ownerships/{id}")
    public ResponseEntity<Ownership> getOwnership(@PathVariable Long id) {
        log.debug("REST request to get Ownership : {}", id);
        Optional<Ownership> ownership = ownershipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ownership);
    }

    /**
     * {@code DELETE  /ownerships/:id} : delete the "id" ownership.
     *
     * @param id the id of the ownership to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ownerships/{id}")
    public ResponseEntity<Void> deleteOwnership(@PathVariable Long id) {
        log.debug("REST request to delete Ownership : {}", id);
        ownershipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
