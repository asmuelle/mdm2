package com.myapp.web.rest;

import com.myapp.domain.Owner;
import com.myapp.repository.OwnerRepository;
import com.myapp.service.OwnerService;
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
 * REST controller for managing {@link com.myapp.domain.Owner}.
 */
@RestController
@RequestMapping("/api")
public class OwnerResource {

    private final Logger log = LoggerFactory.getLogger(OwnerResource.class);

    private static final String ENTITY_NAME = "owner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnerService ownerService;

    private final OwnerRepository ownerRepository;

    public OwnerResource(OwnerService ownerService, OwnerRepository ownerRepository) {
        this.ownerService = ownerService;
        this.ownerRepository = ownerRepository;
    }

    /**
     * {@code POST  /owners} : Create a new owner.
     *
     * @param owner the owner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new owner, or with status {@code 400 (Bad Request)} if the owner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/owners")
    public ResponseEntity<Owner> createOwner(@Valid @RequestBody Owner owner) throws URISyntaxException {
        log.debug("REST request to save Owner : {}", owner);
        if (owner.getId() != null) {
            throw new BadRequestAlertException("A new owner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Owner result = ownerService.save(owner);
        return ResponseEntity
            .created(new URI("/api/owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /owners/:id} : Updates an existing owner.
     *
     * @param id the id of the owner to save.
     * @param owner the owner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated owner,
     * or with status {@code 400 (Bad Request)} if the owner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the owner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/owners/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Owner owner)
        throws URISyntaxException {
        log.debug("REST request to update Owner : {}, {}", id, owner);
        if (owner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, owner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Owner result = ownerService.update(owner);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, owner.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /owners/:id} : Partial updates given fields of an existing owner, field will ignore if it is null
     *
     * @param id the id of the owner to save.
     * @param owner the owner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated owner,
     * or with status {@code 400 (Bad Request)} if the owner is not valid,
     * or with status {@code 404 (Not Found)} if the owner is not found,
     * or with status {@code 500 (Internal Server Error)} if the owner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/owners/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Owner> partialUpdateOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Owner owner
    ) throws URISyntaxException {
        log.debug("REST request to partial update Owner partially : {}, {}", id, owner);
        if (owner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, owner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Owner> result = ownerService.partialUpdate(owner);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, owner.getId().toString())
        );
    }

    /**
     * {@code GET  /owners} : get all the owners.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of owners in body.
     */
    @GetMapping("/owners")
    public List<Owner> getAllOwners() {
        log.debug("REST request to get all Owners");
        return ownerService.findAll();
    }

    /**
     * {@code GET  /owners/:id} : get the "id" owner.
     *
     * @param id the id of the owner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the owner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/owners/{id}")
    public ResponseEntity<Owner> getOwner(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        Optional<Owner> owner = ownerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(owner);
    }

    /**
     * {@code DELETE  /owners/:id} : delete the "id" owner.
     *
     * @param id the id of the owner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/owners/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        log.debug("REST request to delete Owner : {}", id);
        ownerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
