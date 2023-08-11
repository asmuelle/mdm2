package com.myapp.web.rest;

import com.myapp.domain.OwnershipClassification;
import com.myapp.repository.OwnershipClassificationRepository;
import com.myapp.service.OwnershipClassificationQueryService;
import com.myapp.service.OwnershipClassificationService;
import com.myapp.service.criteria.OwnershipClassificationCriteria;
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
 * REST controller for managing {@link com.myapp.domain.OwnershipClassification}.
 */
@RestController
@RequestMapping("/api")
public class OwnershipClassificationResource {

    private final Logger log = LoggerFactory.getLogger(OwnershipClassificationResource.class);

    private static final String ENTITY_NAME = "ownershipClassification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnershipClassificationService ownershipClassificationService;

    private final OwnershipClassificationRepository ownershipClassificationRepository;

    private final OwnershipClassificationQueryService ownershipClassificationQueryService;

    public OwnershipClassificationResource(
        OwnershipClassificationService ownershipClassificationService,
        OwnershipClassificationRepository ownershipClassificationRepository,
        OwnershipClassificationQueryService ownershipClassificationQueryService
    ) {
        this.ownershipClassificationService = ownershipClassificationService;
        this.ownershipClassificationRepository = ownershipClassificationRepository;
        this.ownershipClassificationQueryService = ownershipClassificationQueryService;
    }

    /**
     * {@code POST  /ownership-classifications} : Create a new ownershipClassification.
     *
     * @param ownershipClassification the ownershipClassification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ownershipClassification, or with status {@code 400 (Bad Request)} if the ownershipClassification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ownership-classifications")
    public ResponseEntity<OwnershipClassification> createOwnershipClassification(
        @RequestBody OwnershipClassification ownershipClassification
    ) throws URISyntaxException {
        log.debug("REST request to save OwnershipClassification : {}", ownershipClassification);
        if (ownershipClassification.getId() != null) {
            throw new BadRequestAlertException("A new ownershipClassification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OwnershipClassification result = ownershipClassificationService.save(ownershipClassification);
        return ResponseEntity
            .created(new URI("/api/ownership-classifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ownership-classifications/:id} : Updates an existing ownershipClassification.
     *
     * @param id the id of the ownershipClassification to save.
     * @param ownershipClassification the ownershipClassification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownershipClassification,
     * or with status {@code 400 (Bad Request)} if the ownershipClassification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ownershipClassification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ownership-classifications/{id}")
    public ResponseEntity<OwnershipClassification> updateOwnershipClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OwnershipClassification ownershipClassification
    ) throws URISyntaxException {
        log.debug("REST request to update OwnershipClassification : {}, {}", id, ownershipClassification);
        if (ownershipClassification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownershipClassification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownershipClassificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OwnershipClassification result = ownershipClassificationService.update(ownershipClassification);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownershipClassification.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ownership-classifications/:id} : Partial updates given fields of an existing ownershipClassification, field will ignore if it is null
     *
     * @param id the id of the ownershipClassification to save.
     * @param ownershipClassification the ownershipClassification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownershipClassification,
     * or with status {@code 400 (Bad Request)} if the ownershipClassification is not valid,
     * or with status {@code 404 (Not Found)} if the ownershipClassification is not found,
     * or with status {@code 500 (Internal Server Error)} if the ownershipClassification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ownership-classifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OwnershipClassification> partialUpdateOwnershipClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OwnershipClassification ownershipClassification
    ) throws URISyntaxException {
        log.debug("REST request to partial update OwnershipClassification partially : {}, {}", id, ownershipClassification);
        if (ownershipClassification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownershipClassification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownershipClassificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OwnershipClassification> result = ownershipClassificationService.partialUpdate(ownershipClassification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownershipClassification.getId().toString())
        );
    }

    /**
     * {@code GET  /ownership-classifications} : get all the ownershipClassifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ownershipClassifications in body.
     */
    @GetMapping("/ownership-classifications")
    public ResponseEntity<List<OwnershipClassification>> getAllOwnershipClassifications(OwnershipClassificationCriteria criteria) {
        log.debug("REST request to get OwnershipClassifications by criteria: {}", criteria);

        List<OwnershipClassification> entityList = ownershipClassificationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /ownership-classifications/count} : count all the ownershipClassifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ownership-classifications/count")
    public ResponseEntity<Long> countOwnershipClassifications(OwnershipClassificationCriteria criteria) {
        log.debug("REST request to count OwnershipClassifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(ownershipClassificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ownership-classifications/:id} : get the "id" ownershipClassification.
     *
     * @param id the id of the ownershipClassification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ownershipClassification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ownership-classifications/{id}")
    public ResponseEntity<OwnershipClassification> getOwnershipClassification(@PathVariable Long id) {
        log.debug("REST request to get OwnershipClassification : {}", id);
        Optional<OwnershipClassification> ownershipClassification = ownershipClassificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ownershipClassification);
    }

    /**
     * {@code DELETE  /ownership-classifications/:id} : delete the "id" ownershipClassification.
     *
     * @param id the id of the ownershipClassification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ownership-classifications/{id}")
    public ResponseEntity<Void> deleteOwnershipClassification(@PathVariable Long id) {
        log.debug("REST request to delete OwnershipClassification : {}", id);
        ownershipClassificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
