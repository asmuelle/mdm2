package com.myapp.web.rest;

import com.myapp.domain.OwnershipProperty;
import com.myapp.repository.OwnershipPropertyRepository;
import com.myapp.service.OwnershipPropertyService;
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
 * REST controller for managing {@link com.myapp.domain.OwnershipProperty}.
 */
@RestController
@RequestMapping("/api")
public class OwnershipPropertyResource {

    private final Logger log = LoggerFactory.getLogger(OwnershipPropertyResource.class);

    private static final String ENTITY_NAME = "ownershipProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnershipPropertyService ownershipPropertyService;

    private final OwnershipPropertyRepository ownershipPropertyRepository;

    public OwnershipPropertyResource(
        OwnershipPropertyService ownershipPropertyService,
        OwnershipPropertyRepository ownershipPropertyRepository
    ) {
        this.ownershipPropertyService = ownershipPropertyService;
        this.ownershipPropertyRepository = ownershipPropertyRepository;
    }

    /**
     * {@code POST  /ownership-properties} : Create a new ownershipProperty.
     *
     * @param ownershipProperty the ownershipProperty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ownershipProperty, or with status {@code 400 (Bad Request)} if the ownershipProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ownership-properties")
    public ResponseEntity<OwnershipProperty> createOwnershipProperty(@Valid @RequestBody OwnershipProperty ownershipProperty)
        throws URISyntaxException {
        log.debug("REST request to save OwnershipProperty : {}", ownershipProperty);
        if (ownershipProperty.getId() != null) {
            throw new BadRequestAlertException("A new ownershipProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OwnershipProperty result = ownershipPropertyService.save(ownershipProperty);
        return ResponseEntity
            .created(new URI("/api/ownership-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ownership-properties/:id} : Updates an existing ownershipProperty.
     *
     * @param id the id of the ownershipProperty to save.
     * @param ownershipProperty the ownershipProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownershipProperty,
     * or with status {@code 400 (Bad Request)} if the ownershipProperty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ownershipProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ownership-properties/{id}")
    public ResponseEntity<OwnershipProperty> updateOwnershipProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OwnershipProperty ownershipProperty
    ) throws URISyntaxException {
        log.debug("REST request to update OwnershipProperty : {}, {}", id, ownershipProperty);
        if (ownershipProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownershipProperty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownershipPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OwnershipProperty result = ownershipPropertyService.update(ownershipProperty);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownershipProperty.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ownership-properties/:id} : Partial updates given fields of an existing ownershipProperty, field will ignore if it is null
     *
     * @param id the id of the ownershipProperty to save.
     * @param ownershipProperty the ownershipProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownershipProperty,
     * or with status {@code 400 (Bad Request)} if the ownershipProperty is not valid,
     * or with status {@code 404 (Not Found)} if the ownershipProperty is not found,
     * or with status {@code 500 (Internal Server Error)} if the ownershipProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ownership-properties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OwnershipProperty> partialUpdateOwnershipProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OwnershipProperty ownershipProperty
    ) throws URISyntaxException {
        log.debug("REST request to partial update OwnershipProperty partially : {}, {}", id, ownershipProperty);
        if (ownershipProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownershipProperty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownershipPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OwnershipProperty> result = ownershipPropertyService.partialUpdate(ownershipProperty);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownershipProperty.getId().toString())
        );
    }

    /**
     * {@code GET  /ownership-properties} : get all the ownershipProperties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ownershipProperties in body.
     */
    @GetMapping("/ownership-properties")
    public List<OwnershipProperty> getAllOwnershipProperties() {
        log.debug("REST request to get all OwnershipProperties");
        return ownershipPropertyService.findAll();
    }

    /**
     * {@code GET  /ownership-properties/:id} : get the "id" ownershipProperty.
     *
     * @param id the id of the ownershipProperty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ownershipProperty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ownership-properties/{id}")
    public ResponseEntity<OwnershipProperty> getOwnershipProperty(@PathVariable Long id) {
        log.debug("REST request to get OwnershipProperty : {}", id);
        Optional<OwnershipProperty> ownershipProperty = ownershipPropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ownershipProperty);
    }

    /**
     * {@code DELETE  /ownership-properties/:id} : delete the "id" ownershipProperty.
     *
     * @param id the id of the ownershipProperty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ownership-properties/{id}")
    public ResponseEntity<Void> deleteOwnershipProperty(@PathVariable Long id) {
        log.debug("REST request to delete OwnershipProperty : {}", id);
        ownershipPropertyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
