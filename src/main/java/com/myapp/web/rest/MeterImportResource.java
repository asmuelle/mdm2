package com.myapp.web.rest;

import com.myapp.domain.MeterImport;
import com.myapp.repository.MeterImportRepository;
import com.myapp.service.MeterImportService;
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
 * REST controller for managing {@link com.myapp.domain.MeterImport}.
 */
@RestController
@RequestMapping("/api")
public class MeterImportResource {

    private final Logger log = LoggerFactory.getLogger(MeterImportResource.class);

    private static final String ENTITY_NAME = "meterImport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeterImportService meterImportService;

    private final MeterImportRepository meterImportRepository;

    public MeterImportResource(MeterImportService meterImportService, MeterImportRepository meterImportRepository) {
        this.meterImportService = meterImportService;
        this.meterImportRepository = meterImportRepository;
    }

    /**
     * {@code POST  /meter-imports} : Create a new meterImport.
     *
     * @param meterImport the meterImport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meterImport, or with status {@code 400 (Bad Request)} if the meterImport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meter-imports")
    public ResponseEntity<MeterImport> createMeterImport(@RequestBody MeterImport meterImport) throws URISyntaxException {
        log.debug("REST request to save MeterImport : {}", meterImport);
        if (meterImport.getId() != null) {
            throw new BadRequestAlertException("A new meterImport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeterImport result = meterImportService.save(meterImport);
        return ResponseEntity
            .created(new URI("/api/meter-imports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meter-imports/:id} : Updates an existing meterImport.
     *
     * @param id the id of the meterImport to save.
     * @param meterImport the meterImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meterImport,
     * or with status {@code 400 (Bad Request)} if the meterImport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meterImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meter-imports/{id}")
    public ResponseEntity<MeterImport> updateMeterImport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeterImport meterImport
    ) throws URISyntaxException {
        log.debug("REST request to update MeterImport : {}, {}", id, meterImport);
        if (meterImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meterImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meterImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeterImport result = meterImportService.update(meterImport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meterImport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meter-imports/:id} : Partial updates given fields of an existing meterImport, field will ignore if it is null
     *
     * @param id the id of the meterImport to save.
     * @param meterImport the meterImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meterImport,
     * or with status {@code 400 (Bad Request)} if the meterImport is not valid,
     * or with status {@code 404 (Not Found)} if the meterImport is not found,
     * or with status {@code 500 (Internal Server Error)} if the meterImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meter-imports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeterImport> partialUpdateMeterImport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeterImport meterImport
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeterImport partially : {}, {}", id, meterImport);
        if (meterImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meterImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meterImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeterImport> result = meterImportService.partialUpdate(meterImport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meterImport.getId().toString())
        );
    }

    /**
     * {@code GET  /meter-imports} : get all the meterImports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meterImports in body.
     */
    @GetMapping("/meter-imports")
    public List<MeterImport> getAllMeterImports() {
        log.debug("REST request to get all MeterImports");
        return meterImportService.findAll();
    }

    /**
     * {@code GET  /meter-imports/:id} : get the "id" meterImport.
     *
     * @param id the id of the meterImport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meterImport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meter-imports/{id}")
    public ResponseEntity<MeterImport> getMeterImport(@PathVariable Long id) {
        log.debug("REST request to get MeterImport : {}", id);
        Optional<MeterImport> meterImport = meterImportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(meterImport);
    }

    /**
     * {@code DELETE  /meter-imports/:id} : delete the "id" meterImport.
     *
     * @param id the id of the meterImport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meter-imports/{id}")
    public ResponseEntity<Void> deleteMeterImport(@PathVariable Long id) {
        log.debug("REST request to delete MeterImport : {}", id);
        meterImportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
