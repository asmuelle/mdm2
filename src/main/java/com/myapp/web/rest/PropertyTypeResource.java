package com.myapp.web.rest;

import com.myapp.domain.PropertyType;
import com.myapp.repository.PropertyTypeRepository;
import com.myapp.service.PropertyTypeService;
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
 * REST controller for managing {@link com.myapp.domain.PropertyType}.
 */
@RestController
@RequestMapping("/api")
public class PropertyTypeResource {

    private final Logger log = LoggerFactory.getLogger(PropertyTypeResource.class);

    private static final String ENTITY_NAME = "propertyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyTypeService propertyTypeService;

    private final PropertyTypeRepository propertyTypeRepository;

    public PropertyTypeResource(PropertyTypeService propertyTypeService, PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeService = propertyTypeService;
        this.propertyTypeRepository = propertyTypeRepository;
    }

    /**
     * {@code POST  /property-types} : Create a new propertyType.
     *
     * @param propertyType the propertyType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyType, or with status {@code 400 (Bad Request)} if the propertyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/property-types")
    public ResponseEntity<PropertyType> createPropertyType(@Valid @RequestBody PropertyType propertyType) throws URISyntaxException {
        log.debug("REST request to save PropertyType : {}", propertyType);
        if (propertyType.getId() != null) {
            throw new BadRequestAlertException("A new propertyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyType result = propertyTypeService.save(propertyType);
        return ResponseEntity
            .created(new URI("/api/property-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /property-types/:id} : Updates an existing propertyType.
     *
     * @param id the id of the propertyType to save.
     * @param propertyType the propertyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyType,
     * or with status {@code 400 (Bad Request)} if the propertyType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/property-types/{id}")
    public ResponseEntity<PropertyType> updatePropertyType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PropertyType propertyType
    ) throws URISyntaxException {
        log.debug("REST request to update PropertyType : {}, {}", id, propertyType);
        if (propertyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PropertyType result = propertyTypeService.update(propertyType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /property-types/:id} : Partial updates given fields of an existing propertyType, field will ignore if it is null
     *
     * @param id the id of the propertyType to save.
     * @param propertyType the propertyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyType,
     * or with status {@code 400 (Bad Request)} if the propertyType is not valid,
     * or with status {@code 404 (Not Found)} if the propertyType is not found,
     * or with status {@code 500 (Internal Server Error)} if the propertyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/property-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PropertyType> partialUpdatePropertyType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PropertyType propertyType
    ) throws URISyntaxException {
        log.debug("REST request to partial update PropertyType partially : {}, {}", id, propertyType);
        if (propertyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PropertyType> result = propertyTypeService.partialUpdate(propertyType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyType.getId().toString())
        );
    }

    /**
     * {@code GET  /property-types} : get all the propertyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propertyTypes in body.
     */
    @GetMapping("/property-types")
    public List<PropertyType> getAllPropertyTypes() {
        log.debug("REST request to get all PropertyTypes");
        return propertyTypeService.findAll();
    }

    /**
     * {@code GET  /property-types/:id} : get the "id" propertyType.
     *
     * @param id the id of the propertyType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/property-types/{id}")
    public ResponseEntity<PropertyType> getPropertyType(@PathVariable Long id) {
        log.debug("REST request to get PropertyType : {}", id);
        Optional<PropertyType> propertyType = propertyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propertyType);
    }

    /**
     * {@code DELETE  /property-types/:id} : delete the "id" propertyType.
     *
     * @param id the id of the propertyType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/property-types/{id}")
    public ResponseEntity<Void> deletePropertyType(@PathVariable Long id) {
        log.debug("REST request to delete PropertyType : {}", id);
        propertyTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
