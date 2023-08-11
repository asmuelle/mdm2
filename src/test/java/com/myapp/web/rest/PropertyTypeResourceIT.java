package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PropertyType;
import com.myapp.repository.PropertyTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PropertyTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PropertyTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_PATTERN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/property-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropertyTypeMockMvc;

    private PropertyType propertyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyType createEntity(EntityManager em) {
        PropertyType propertyType = new PropertyType().name(DEFAULT_NAME).pattern(DEFAULT_PATTERN);
        return propertyType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyType createUpdatedEntity(EntityManager em) {
        PropertyType propertyType = new PropertyType().name(UPDATED_NAME).pattern(UPDATED_PATTERN);
        return propertyType;
    }

    @BeforeEach
    public void initTest() {
        propertyType = createEntity(em);
    }

    @Test
    @Transactional
    void createPropertyType() throws Exception {
        int databaseSizeBeforeCreate = propertyTypeRepository.findAll().size();
        // Create the PropertyType
        restPropertyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyType)))
            .andExpect(status().isCreated());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PropertyType testPropertyType = propertyTypeList.get(propertyTypeList.size() - 1);
        assertThat(testPropertyType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPropertyType.getPattern()).isEqualTo(DEFAULT_PATTERN);
    }

    @Test
    @Transactional
    void createPropertyTypeWithExistingId() throws Exception {
        // Create the PropertyType with an existing ID
        propertyType.setId(1L);

        int databaseSizeBeforeCreate = propertyTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyType)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyTypeRepository.findAll().size();
        // set the field null
        propertyType.setName(null);

        // Create the PropertyType, which fails.

        restPropertyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyType)))
            .andExpect(status().isBadRequest());

        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPatternIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyTypeRepository.findAll().size();
        // set the field null
        propertyType.setPattern(null);

        // Create the PropertyType, which fails.

        restPropertyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyType)))
            .andExpect(status().isBadRequest());

        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPropertyTypes() throws Exception {
        // Initialize the database
        propertyTypeRepository.saveAndFlush(propertyType);

        // Get all the propertyTypeList
        restPropertyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pattern").value(hasItem(DEFAULT_PATTERN)));
    }

    @Test
    @Transactional
    void getPropertyType() throws Exception {
        // Initialize the database
        propertyTypeRepository.saveAndFlush(propertyType);

        // Get the propertyType
        restPropertyTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, propertyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(propertyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pattern").value(DEFAULT_PATTERN));
    }

    @Test
    @Transactional
    void getNonExistingPropertyType() throws Exception {
        // Get the propertyType
        restPropertyTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPropertyType() throws Exception {
        // Initialize the database
        propertyTypeRepository.saveAndFlush(propertyType);

        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();

        // Update the propertyType
        PropertyType updatedPropertyType = propertyTypeRepository.findById(propertyType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPropertyType are not directly saved in db
        em.detach(updatedPropertyType);
        updatedPropertyType.name(UPDATED_NAME).pattern(UPDATED_PATTERN);

        restPropertyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPropertyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPropertyType))
            )
            .andExpect(status().isOk());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
        PropertyType testPropertyType = propertyTypeList.get(propertyTypeList.size() - 1);
        assertThat(testPropertyType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPropertyType.getPattern()).isEqualTo(UPDATED_PATTERN);
    }

    @Test
    @Transactional
    void putNonExistingPropertyType() throws Exception {
        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();
        propertyType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, propertyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(propertyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPropertyType() throws Exception {
        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();
        propertyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(propertyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPropertyType() throws Exception {
        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();
        propertyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePropertyTypeWithPatch() throws Exception {
        // Initialize the database
        propertyTypeRepository.saveAndFlush(propertyType);

        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();

        // Update the propertyType using partial update
        PropertyType partialUpdatedPropertyType = new PropertyType();
        partialUpdatedPropertyType.setId(propertyType.getId());

        partialUpdatedPropertyType.name(UPDATED_NAME).pattern(UPDATED_PATTERN);

        restPropertyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPropertyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPropertyType))
            )
            .andExpect(status().isOk());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
        PropertyType testPropertyType = propertyTypeList.get(propertyTypeList.size() - 1);
        assertThat(testPropertyType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPropertyType.getPattern()).isEqualTo(UPDATED_PATTERN);
    }

    @Test
    @Transactional
    void fullUpdatePropertyTypeWithPatch() throws Exception {
        // Initialize the database
        propertyTypeRepository.saveAndFlush(propertyType);

        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();

        // Update the propertyType using partial update
        PropertyType partialUpdatedPropertyType = new PropertyType();
        partialUpdatedPropertyType.setId(propertyType.getId());

        partialUpdatedPropertyType.name(UPDATED_NAME).pattern(UPDATED_PATTERN);

        restPropertyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPropertyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPropertyType))
            )
            .andExpect(status().isOk());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
        PropertyType testPropertyType = propertyTypeList.get(propertyTypeList.size() - 1);
        assertThat(testPropertyType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPropertyType.getPattern()).isEqualTo(UPDATED_PATTERN);
    }

    @Test
    @Transactional
    void patchNonExistingPropertyType() throws Exception {
        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();
        propertyType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, propertyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(propertyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPropertyType() throws Exception {
        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();
        propertyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(propertyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPropertyType() throws Exception {
        int databaseSizeBeforeUpdate = propertyTypeRepository.findAll().size();
        propertyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(propertyType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PropertyType in the database
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePropertyType() throws Exception {
        // Initialize the database
        propertyTypeRepository.saveAndFlush(propertyType);

        int databaseSizeBeforeDelete = propertyTypeRepository.findAll().size();

        // Delete the propertyType
        restPropertyTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, propertyType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PropertyType> propertyTypeList = propertyTypeRepository.findAll();
        assertThat(propertyTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
