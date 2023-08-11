package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.OwnershipProperty;
import com.myapp.repository.OwnershipPropertyRepository;
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
 * Integration tests for the {@link OwnershipPropertyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnershipPropertyResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ownership-properties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnershipPropertyRepository ownershipPropertyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnershipPropertyMockMvc;

    private OwnershipProperty ownershipProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OwnershipProperty createEntity(EntityManager em) {
        OwnershipProperty ownershipProperty = new OwnershipProperty().value(DEFAULT_VALUE);
        return ownershipProperty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OwnershipProperty createUpdatedEntity(EntityManager em) {
        OwnershipProperty ownershipProperty = new OwnershipProperty().value(UPDATED_VALUE);
        return ownershipProperty;
    }

    @BeforeEach
    public void initTest() {
        ownershipProperty = createEntity(em);
    }

    @Test
    @Transactional
    void createOwnershipProperty() throws Exception {
        int databaseSizeBeforeCreate = ownershipPropertyRepository.findAll().size();
        // Create the OwnershipProperty
        restOwnershipPropertyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isCreated());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        OwnershipProperty testOwnershipProperty = ownershipPropertyList.get(ownershipPropertyList.size() - 1);
        assertThat(testOwnershipProperty.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createOwnershipPropertyWithExistingId() throws Exception {
        // Create the OwnershipProperty with an existing ID
        ownershipProperty.setId(1L);

        int databaseSizeBeforeCreate = ownershipPropertyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnershipPropertyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownershipPropertyRepository.findAll().size();
        // set the field null
        ownershipProperty.setValue(null);

        // Create the OwnershipProperty, which fails.

        restOwnershipPropertyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isBadRequest());

        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOwnershipProperties() throws Exception {
        // Initialize the database
        ownershipPropertyRepository.saveAndFlush(ownershipProperty);

        // Get all the ownershipPropertyList
        restOwnershipPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownershipProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getOwnershipProperty() throws Exception {
        // Initialize the database
        ownershipPropertyRepository.saveAndFlush(ownershipProperty);

        // Get the ownershipProperty
        restOwnershipPropertyMockMvc
            .perform(get(ENTITY_API_URL_ID, ownershipProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ownershipProperty.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingOwnershipProperty() throws Exception {
        // Get the ownershipProperty
        restOwnershipPropertyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwnershipProperty() throws Exception {
        // Initialize the database
        ownershipPropertyRepository.saveAndFlush(ownershipProperty);

        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();

        // Update the ownershipProperty
        OwnershipProperty updatedOwnershipProperty = ownershipPropertyRepository.findById(ownershipProperty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOwnershipProperty are not directly saved in db
        em.detach(updatedOwnershipProperty);
        updatedOwnershipProperty.value(UPDATED_VALUE);

        restOwnershipPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOwnershipProperty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOwnershipProperty))
            )
            .andExpect(status().isOk());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
        OwnershipProperty testOwnershipProperty = ownershipPropertyList.get(ownershipPropertyList.size() - 1);
        assertThat(testOwnershipProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingOwnershipProperty() throws Exception {
        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();
        ownershipProperty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnershipPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownershipProperty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwnershipProperty() throws Exception {
        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();
        ownershipProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwnershipProperty() throws Exception {
        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();
        ownershipProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipPropertyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnershipPropertyWithPatch() throws Exception {
        // Initialize the database
        ownershipPropertyRepository.saveAndFlush(ownershipProperty);

        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();

        // Update the ownershipProperty using partial update
        OwnershipProperty partialUpdatedOwnershipProperty = new OwnershipProperty();
        partialUpdatedOwnershipProperty.setId(ownershipProperty.getId());

        partialUpdatedOwnershipProperty.value(UPDATED_VALUE);

        restOwnershipPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwnershipProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwnershipProperty))
            )
            .andExpect(status().isOk());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
        OwnershipProperty testOwnershipProperty = ownershipPropertyList.get(ownershipPropertyList.size() - 1);
        assertThat(testOwnershipProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateOwnershipPropertyWithPatch() throws Exception {
        // Initialize the database
        ownershipPropertyRepository.saveAndFlush(ownershipProperty);

        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();

        // Update the ownershipProperty using partial update
        OwnershipProperty partialUpdatedOwnershipProperty = new OwnershipProperty();
        partialUpdatedOwnershipProperty.setId(ownershipProperty.getId());

        partialUpdatedOwnershipProperty.value(UPDATED_VALUE);

        restOwnershipPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwnershipProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwnershipProperty))
            )
            .andExpect(status().isOk());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
        OwnershipProperty testOwnershipProperty = ownershipPropertyList.get(ownershipPropertyList.size() - 1);
        assertThat(testOwnershipProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingOwnershipProperty() throws Exception {
        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();
        ownershipProperty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnershipPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ownershipProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwnershipProperty() throws Exception {
        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();
        ownershipProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwnershipProperty() throws Exception {
        int databaseSizeBeforeUpdate = ownershipPropertyRepository.findAll().size();
        ownershipProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownershipProperty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OwnershipProperty in the database
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwnershipProperty() throws Exception {
        // Initialize the database
        ownershipPropertyRepository.saveAndFlush(ownershipProperty);

        int databaseSizeBeforeDelete = ownershipPropertyRepository.findAll().size();

        // Delete the ownershipProperty
        restOwnershipPropertyMockMvc
            .perform(delete(ENTITY_API_URL_ID, ownershipProperty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OwnershipProperty> ownershipPropertyList = ownershipPropertyRepository.findAll();
        assertThat(ownershipPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
