package com.myapp.web.rest;

import com.myapp.domain.Meter;
import com.myapp.repository.MeterRepository;
import com.myapp.service.MeterQueryService;
import com.myapp.service.MeterService;
import com.myapp.service.criteria.MeterCriteria;
import com.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.myapp.domain.Meter}.
 */
@RestController
@RequestMapping("/api")
public class MeterResource {

    private final Logger log = LoggerFactory.getLogger(MeterResource.class);

    private static final String ENTITY_NAME = "meter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeterService meterService;

    private final MeterRepository meterRepository;

    private final MeterQueryService meterQueryService;

    public MeterResource(MeterService meterService, MeterRepository meterRepository, MeterQueryService meterQueryService) {
        this.meterService = meterService;
        this.meterRepository = meterRepository;
        this.meterQueryService = meterQueryService;
    }

    /**
     * {@code POST  /meters} : Create a new meter.
     *
     * @param meter the meter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meter, or with status {@code 400 (Bad Request)} if the meter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meters")
    public ResponseEntity<Meter> createMeter(@RequestBody Meter meter) throws URISyntaxException {
        log.debug("REST request to save Meter : {}", meter);
        if (meter.getId() != null) {
            throw new BadRequestAlertException("A new meter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Meter result = meterService.save(meter);
        return ResponseEntity
            .created(new URI("/api/meters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meters/:id} : Updates an existing meter.
     *
     * @param id the id of the meter to save.
     * @param meter the meter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meter,
     * or with status {@code 400 (Bad Request)} if the meter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meters/{id}")
    public ResponseEntity<Meter> updateMeter(@PathVariable(value = "id", required = false) final Long id, @RequestBody Meter meter)
        throws URISyntaxException {
        log.debug("REST request to update Meter : {}, {}", id, meter);
        if (meter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Meter result = meterService.update(meter);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meter.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meters/:id} : Partial updates given fields of an existing meter, field will ignore if it is null
     *
     * @param id the id of the meter to save.
     * @param meter the meter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meter,
     * or with status {@code 400 (Bad Request)} if the meter is not valid,
     * or with status {@code 404 (Not Found)} if the meter is not found,
     * or with status {@code 500 (Internal Server Error)} if the meter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Meter> partialUpdateMeter(@PathVariable(value = "id", required = false) final Long id, @RequestBody Meter meter)
        throws URISyntaxException {
        log.debug("REST request to partial update Meter partially : {}, {}", id, meter);
        if (meter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Meter> result = meterService.partialUpdate(meter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meter.getId().toString())
        );
    }

    /**
     * {@code GET  /meters} : get all the meters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meters in body.
     */
    @GetMapping("/meters")
    public ResponseEntity<List<Meter>> getAllMeters(
        MeterCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Meters by criteria: {}", criteria);

        Page<Meter> page = meterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meters/count} : count all the meters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/meters/count")
    public ResponseEntity<Long> countMeters(MeterCriteria criteria) {
        log.debug("REST request to count Meters by criteria: {}", criteria);
        return ResponseEntity.ok().body(meterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meters/:id} : get the "id" meter.
     *
     * @param id the id of the meter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meters/{id}")
    public ResponseEntity<Meter> getMeter(@PathVariable Long id) {
        log.debug("REST request to get Meter : {}", id);
        Optional<Meter> meter = meterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(meter);
    }

    /**
     * {@code DELETE  /meters/:id} : delete the "id" meter.
     *
     * @param id the id of the meter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meters/{id}")
    public ResponseEntity<Void> deleteMeter(@PathVariable Long id) {
        log.debug("REST request to delete Meter : {}", id);
        meterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
