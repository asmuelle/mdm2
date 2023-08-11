package com.myapp.web.rest;

import com.myapp.domain.WasteTrackingParameters;
import com.myapp.repository.WasteTrackingParametersRepository;
import com.myapp.service.WasteTrackingParametersService;
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
 * REST controller for managing {@link com.myapp.domain.WasteTrackingParameters}.
 */
@RestController
@RequestMapping("/api")
public class WasteTrackingParametersResource {

    private final Logger log = LoggerFactory.getLogger(WasteTrackingParametersResource.class);

    private static final String ENTITY_NAME = "wasteTrackingParameters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WasteTrackingParametersService wasteTrackingParametersService;

    private final WasteTrackingParametersRepository wasteTrackingParametersRepository;

    public WasteTrackingParametersResource(
        WasteTrackingParametersService wasteTrackingParametersService,
        WasteTrackingParametersRepository wasteTrackingParametersRepository
    ) {
        this.wasteTrackingParametersService = wasteTrackingParametersService;
        this.wasteTrackingParametersRepository = wasteTrackingParametersRepository;
    }

    /**
     * {@code POST  /waste-tracking-parameters} : Create a new wasteTrackingParameters.
     *
     * @param wasteTrackingParameters the wasteTrackingParameters to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wasteTrackingParameters, or with status {@code 400 (Bad Request)} if the wasteTrackingParameters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/waste-tracking-parameters")
    public ResponseEntity<WasteTrackingParameters> createWasteTrackingParameters(
        @Valid @RequestBody WasteTrackingParameters wasteTrackingParameters
    ) throws URISyntaxException {
        log.debug("REST request to save WasteTrackingParameters : {}", wasteTrackingParameters);
        if (wasteTrackingParameters.getId() != null) {
            throw new BadRequestAlertException("A new wasteTrackingParameters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WasteTrackingParameters result = wasteTrackingParametersService.save(wasteTrackingParameters);
        return ResponseEntity
            .created(new URI("/api/waste-tracking-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waste-tracking-parameters/:id} : Updates an existing wasteTrackingParameters.
     *
     * @param id the id of the wasteTrackingParameters to save.
     * @param wasteTrackingParameters the wasteTrackingParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wasteTrackingParameters,
     * or with status {@code 400 (Bad Request)} if the wasteTrackingParameters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wasteTrackingParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/waste-tracking-parameters/{id}")
    public ResponseEntity<WasteTrackingParameters> updateWasteTrackingParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WasteTrackingParameters wasteTrackingParameters
    ) throws URISyntaxException {
        log.debug("REST request to update WasteTrackingParameters : {}, {}", id, wasteTrackingParameters);
        if (wasteTrackingParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wasteTrackingParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wasteTrackingParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WasteTrackingParameters result = wasteTrackingParametersService.update(wasteTrackingParameters);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wasteTrackingParameters.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /waste-tracking-parameters/:id} : Partial updates given fields of an existing wasteTrackingParameters, field will ignore if it is null
     *
     * @param id the id of the wasteTrackingParameters to save.
     * @param wasteTrackingParameters the wasteTrackingParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wasteTrackingParameters,
     * or with status {@code 400 (Bad Request)} if the wasteTrackingParameters is not valid,
     * or with status {@code 404 (Not Found)} if the wasteTrackingParameters is not found,
     * or with status {@code 500 (Internal Server Error)} if the wasteTrackingParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/waste-tracking-parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WasteTrackingParameters> partialUpdateWasteTrackingParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WasteTrackingParameters wasteTrackingParameters
    ) throws URISyntaxException {
        log.debug("REST request to partial update WasteTrackingParameters partially : {}, {}", id, wasteTrackingParameters);
        if (wasteTrackingParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wasteTrackingParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wasteTrackingParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WasteTrackingParameters> result = wasteTrackingParametersService.partialUpdate(wasteTrackingParameters);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wasteTrackingParameters.getId().toString())
        );
    }

    /**
     * {@code GET  /waste-tracking-parameters} : get all the wasteTrackingParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wasteTrackingParameters in body.
     */
    @GetMapping("/waste-tracking-parameters")
    public List<WasteTrackingParameters> getAllWasteTrackingParameters() {
        log.debug("REST request to get all WasteTrackingParameters");
        return wasteTrackingParametersService.findAll();
    }

    /**
     * {@code GET  /waste-tracking-parameters/:id} : get the "id" wasteTrackingParameters.
     *
     * @param id the id of the wasteTrackingParameters to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wasteTrackingParameters, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/waste-tracking-parameters/{id}")
    public ResponseEntity<WasteTrackingParameters> getWasteTrackingParameters(@PathVariable Long id) {
        log.debug("REST request to get WasteTrackingParameters : {}", id);
        Optional<WasteTrackingParameters> wasteTrackingParameters = wasteTrackingParametersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wasteTrackingParameters);
    }

    /**
     * {@code DELETE  /waste-tracking-parameters/:id} : delete the "id" wasteTrackingParameters.
     *
     * @param id the id of the wasteTrackingParameters to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/waste-tracking-parameters/{id}")
    public ResponseEntity<Void> deleteWasteTrackingParameters(@PathVariable Long id) {
        log.debug("REST request to delete WasteTrackingParameters : {}", id);
        wasteTrackingParametersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
