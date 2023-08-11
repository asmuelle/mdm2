package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Ownership;
import com.myapp.domain.OwnershipClassification;
import com.myapp.repository.OwnershipClassificationRepository;
import com.myapp.service.criteria.OwnershipClassificationCriteria;
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
 * Integration tests for the {@link OwnershipClassificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnershipClassificationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ownership-classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnershipClassificationRepository ownershipClassificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnershipClassificationMockMvc;

    private OwnershipClassification ownershipClassification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OwnershipClassification createEntity(EntityManager em) {
        OwnershipClassification ownershipClassification = new OwnershipClassification().name(DEFAULT_NAME);
        return ownershipClassification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OwnershipClassification createUpdatedEntity(EntityManager em) {
        OwnershipClassification ownershipClassification = new OwnershipClassification().name(UPDATED_NAME);
        return ownershipClassification;
    }

    @BeforeEach
    public void initTest() {
        ownershipClassification = createEntity(em);
    }

    @Test
    @Transactional
    void createOwnershipClassification() throws Exception {
        int databaseSizeBeforeCreate = ownershipClassificationRepository.findAll().size();
        // Create the OwnershipClassification
        restOwnershipClassificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isCreated());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeCreate + 1);
        OwnershipClassification testOwnershipClassification = ownershipClassificationList.get(ownershipClassificationList.size() - 1);
        assertThat(testOwnershipClassification.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOwnershipClassificationWithExistingId() throws Exception {
        // Create the OwnershipClassification with an existing ID
        ownershipClassification.setId(1L);

        int databaseSizeBeforeCreate = ownershipClassificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnershipClassificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOwnershipClassifications() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get all the ownershipClassificationList
        restOwnershipClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownershipClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOwnershipClassification() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get the ownershipClassification
        restOwnershipClassificationMockMvc
            .perform(get(ENTITY_API_URL_ID, ownershipClassification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ownershipClassification.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getOwnershipClassificationsByIdFiltering() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        Long id = ownershipClassification.getId();

        defaultOwnershipClassificationShouldBeFound("id.equals=" + id);
        defaultOwnershipClassificationShouldNotBeFound("id.notEquals=" + id);

        defaultOwnershipClassificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOwnershipClassificationShouldNotBeFound("id.greaterThan=" + id);

        defaultOwnershipClassificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOwnershipClassificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOwnershipClassificationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get all the ownershipClassificationList where name equals to DEFAULT_NAME
        defaultOwnershipClassificationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ownershipClassificationList where name equals to UPDATED_NAME
        defaultOwnershipClassificationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipClassificationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get all the ownershipClassificationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOwnershipClassificationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ownershipClassificationList where name equals to UPDATED_NAME
        defaultOwnershipClassificationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipClassificationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get all the ownershipClassificationList where name is not null
        defaultOwnershipClassificationShouldBeFound("name.specified=true");

        // Get all the ownershipClassificationList where name is null
        defaultOwnershipClassificationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnershipClassificationsByNameContainsSomething() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get all the ownershipClassificationList where name contains DEFAULT_NAME
        defaultOwnershipClassificationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ownershipClassificationList where name contains UPDATED_NAME
        defaultOwnershipClassificationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipClassificationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        // Get all the ownershipClassificationList where name does not contain DEFAULT_NAME
        defaultOwnershipClassificationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ownershipClassificationList where name does not contain UPDATED_NAME
        defaultOwnershipClassificationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipClassificationsByOwnershipsIsEqualToSomething() throws Exception {
        Ownership ownerships;
        if (TestUtil.findAll(em, Ownership.class).isEmpty()) {
            ownershipClassificationRepository.saveAndFlush(ownershipClassification);
            ownerships = OwnershipResourceIT.createEntity(em);
        } else {
            ownerships = TestUtil.findAll(em, Ownership.class).get(0);
        }
        em.persist(ownerships);
        em.flush();
        ownershipClassification.addOwnerships(ownerships);
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);
        Long ownershipsId = ownerships.getId();
        // Get all the ownershipClassificationList where ownerships equals to ownershipsId
        defaultOwnershipClassificationShouldBeFound("ownershipsId.equals=" + ownershipsId);

        // Get all the ownershipClassificationList where ownerships equals to (ownershipsId + 1)
        defaultOwnershipClassificationShouldNotBeFound("ownershipsId.equals=" + (ownershipsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOwnershipClassificationShouldBeFound(String filter) throws Exception {
        restOwnershipClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownershipClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restOwnershipClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOwnershipClassificationShouldNotBeFound(String filter) throws Exception {
        restOwnershipClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOwnershipClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOwnershipClassification() throws Exception {
        // Get the ownershipClassification
        restOwnershipClassificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwnershipClassification() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();

        // Update the ownershipClassification
        OwnershipClassification updatedOwnershipClassification = ownershipClassificationRepository
            .findById(ownershipClassification.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedOwnershipClassification are not directly saved in db
        em.detach(updatedOwnershipClassification);
        updatedOwnershipClassification.name(UPDATED_NAME);

        restOwnershipClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOwnershipClassification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOwnershipClassification))
            )
            .andExpect(status().isOk());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
        OwnershipClassification testOwnershipClassification = ownershipClassificationList.get(ownershipClassificationList.size() - 1);
        assertThat(testOwnershipClassification.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOwnershipClassification() throws Exception {
        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();
        ownershipClassification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnershipClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownershipClassification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwnershipClassification() throws Exception {
        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();
        ownershipClassification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwnershipClassification() throws Exception {
        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();
        ownershipClassification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipClassificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnershipClassificationWithPatch() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();

        // Update the ownershipClassification using partial update
        OwnershipClassification partialUpdatedOwnershipClassification = new OwnershipClassification();
        partialUpdatedOwnershipClassification.setId(ownershipClassification.getId());

        restOwnershipClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwnershipClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwnershipClassification))
            )
            .andExpect(status().isOk());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
        OwnershipClassification testOwnershipClassification = ownershipClassificationList.get(ownershipClassificationList.size() - 1);
        assertThat(testOwnershipClassification.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOwnershipClassificationWithPatch() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();

        // Update the ownershipClassification using partial update
        OwnershipClassification partialUpdatedOwnershipClassification = new OwnershipClassification();
        partialUpdatedOwnershipClassification.setId(ownershipClassification.getId());

        partialUpdatedOwnershipClassification.name(UPDATED_NAME);

        restOwnershipClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwnershipClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwnershipClassification))
            )
            .andExpect(status().isOk());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
        OwnershipClassification testOwnershipClassification = ownershipClassificationList.get(ownershipClassificationList.size() - 1);
        assertThat(testOwnershipClassification.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOwnershipClassification() throws Exception {
        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();
        ownershipClassification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnershipClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ownershipClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwnershipClassification() throws Exception {
        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();
        ownershipClassification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isBadRequest());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwnershipClassification() throws Exception {
        int databaseSizeBeforeUpdate = ownershipClassificationRepository.findAll().size();
        ownershipClassification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownershipClassification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OwnershipClassification in the database
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwnershipClassification() throws Exception {
        // Initialize the database
        ownershipClassificationRepository.saveAndFlush(ownershipClassification);

        int databaseSizeBeforeDelete = ownershipClassificationRepository.findAll().size();

        // Delete the ownershipClassification
        restOwnershipClassificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, ownershipClassification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OwnershipClassification> ownershipClassificationList = ownershipClassificationRepository.findAll();
        assertThat(ownershipClassificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
