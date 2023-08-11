package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.WasteTrackingParameters;
import com.myapp.repository.WasteTrackingParametersRepository;
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
 * Integration tests for the {@link WasteTrackingParametersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WasteTrackingParametersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_WASTE_ISSUE_CREATION_THRESHOLD = 1F;
    private static final Float UPDATED_WASTE_ISSUE_CREATION_THRESHOLD = 2F;

    private static final Integer DEFAULT_MAX_WASTE_ISSUE_CREATION_RATE = 1;
    private static final Integer UPDATED_MAX_WASTE_ISSUE_CREATION_RATE = 2;

    private static final Integer DEFAULT_MAX_ACTIVE_WASTE_ISSUES = 1;
    private static final Integer UPDATED_MAX_ACTIVE_WASTE_ISSUES = 2;

    private static final Boolean DEFAULT_AUTO_CREATE_WASTE_ISSUES = false;
    private static final Boolean UPDATED_AUTO_CREATE_WASTE_ISSUES = true;

    private static final String ENTITY_API_URL = "/api/waste-tracking-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WasteTrackingParametersRepository wasteTrackingParametersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWasteTrackingParametersMockMvc;

    private WasteTrackingParameters wasteTrackingParameters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WasteTrackingParameters createEntity(EntityManager em) {
        WasteTrackingParameters wasteTrackingParameters = new WasteTrackingParameters()
            .name(DEFAULT_NAME)
            .wasteIssueCreationThreshold(DEFAULT_WASTE_ISSUE_CREATION_THRESHOLD)
            .maxWasteIssueCreationRate(DEFAULT_MAX_WASTE_ISSUE_CREATION_RATE)
            .maxActiveWasteIssues(DEFAULT_MAX_ACTIVE_WASTE_ISSUES)
            .autoCreateWasteIssues(DEFAULT_AUTO_CREATE_WASTE_ISSUES);
        return wasteTrackingParameters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WasteTrackingParameters createUpdatedEntity(EntityManager em) {
        WasteTrackingParameters wasteTrackingParameters = new WasteTrackingParameters()
            .name(UPDATED_NAME)
            .wasteIssueCreationThreshold(UPDATED_WASTE_ISSUE_CREATION_THRESHOLD)
            .maxWasteIssueCreationRate(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE)
            .maxActiveWasteIssues(UPDATED_MAX_ACTIVE_WASTE_ISSUES)
            .autoCreateWasteIssues(UPDATED_AUTO_CREATE_WASTE_ISSUES);
        return wasteTrackingParameters;
    }

    @BeforeEach
    public void initTest() {
        wasteTrackingParameters = createEntity(em);
    }

    @Test
    @Transactional
    void createWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeCreate = wasteTrackingParametersRepository.findAll().size();
        // Create the WasteTrackingParameters
        restWasteTrackingParametersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isCreated());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeCreate + 1);
        WasteTrackingParameters testWasteTrackingParameters = wasteTrackingParametersList.get(wasteTrackingParametersList.size() - 1);
        assertThat(testWasteTrackingParameters.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWasteTrackingParameters.getWasteIssueCreationThreshold()).isEqualTo(DEFAULT_WASTE_ISSUE_CREATION_THRESHOLD);
        assertThat(testWasteTrackingParameters.getMaxWasteIssueCreationRate()).isEqualTo(DEFAULT_MAX_WASTE_ISSUE_CREATION_RATE);
        assertThat(testWasteTrackingParameters.getMaxActiveWasteIssues()).isEqualTo(DEFAULT_MAX_ACTIVE_WASTE_ISSUES);
        assertThat(testWasteTrackingParameters.getAutoCreateWasteIssues()).isEqualTo(DEFAULT_AUTO_CREATE_WASTE_ISSUES);
    }

    @Test
    @Transactional
    void createWasteTrackingParametersWithExistingId() throws Exception {
        // Create the WasteTrackingParameters with an existing ID
        wasteTrackingParameters.setId(1L);

        int databaseSizeBeforeCreate = wasteTrackingParametersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWasteTrackingParametersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wasteTrackingParametersRepository.findAll().size();
        // set the field null
        wasteTrackingParameters.setName(null);

        // Create the WasteTrackingParameters, which fails.

        restWasteTrackingParametersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isBadRequest());

        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWasteTrackingParameters() throws Exception {
        // Initialize the database
        wasteTrackingParametersRepository.saveAndFlush(wasteTrackingParameters);

        // Get all the wasteTrackingParametersList
        restWasteTrackingParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wasteTrackingParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].wasteIssueCreationThreshold").value(hasItem(DEFAULT_WASTE_ISSUE_CREATION_THRESHOLD.doubleValue())))
            .andExpect(jsonPath("$.[*].maxWasteIssueCreationRate").value(hasItem(DEFAULT_MAX_WASTE_ISSUE_CREATION_RATE)))
            .andExpect(jsonPath("$.[*].maxActiveWasteIssues").value(hasItem(DEFAULT_MAX_ACTIVE_WASTE_ISSUES)))
            .andExpect(jsonPath("$.[*].autoCreateWasteIssues").value(hasItem(DEFAULT_AUTO_CREATE_WASTE_ISSUES.booleanValue())));
    }

    @Test
    @Transactional
    void getWasteTrackingParameters() throws Exception {
        // Initialize the database
        wasteTrackingParametersRepository.saveAndFlush(wasteTrackingParameters);

        // Get the wasteTrackingParameters
        restWasteTrackingParametersMockMvc
            .perform(get(ENTITY_API_URL_ID, wasteTrackingParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wasteTrackingParameters.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.wasteIssueCreationThreshold").value(DEFAULT_WASTE_ISSUE_CREATION_THRESHOLD.doubleValue()))
            .andExpect(jsonPath("$.maxWasteIssueCreationRate").value(DEFAULT_MAX_WASTE_ISSUE_CREATION_RATE))
            .andExpect(jsonPath("$.maxActiveWasteIssues").value(DEFAULT_MAX_ACTIVE_WASTE_ISSUES))
            .andExpect(jsonPath("$.autoCreateWasteIssues").value(DEFAULT_AUTO_CREATE_WASTE_ISSUES.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingWasteTrackingParameters() throws Exception {
        // Get the wasteTrackingParameters
        restWasteTrackingParametersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWasteTrackingParameters() throws Exception {
        // Initialize the database
        wasteTrackingParametersRepository.saveAndFlush(wasteTrackingParameters);

        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();

        // Update the wasteTrackingParameters
        WasteTrackingParameters updatedWasteTrackingParameters = wasteTrackingParametersRepository
            .findById(wasteTrackingParameters.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedWasteTrackingParameters are not directly saved in db
        em.detach(updatedWasteTrackingParameters);
        updatedWasteTrackingParameters
            .name(UPDATED_NAME)
            .wasteIssueCreationThreshold(UPDATED_WASTE_ISSUE_CREATION_THRESHOLD)
            .maxWasteIssueCreationRate(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE)
            .maxActiveWasteIssues(UPDATED_MAX_ACTIVE_WASTE_ISSUES)
            .autoCreateWasteIssues(UPDATED_AUTO_CREATE_WASTE_ISSUES);

        restWasteTrackingParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWasteTrackingParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWasteTrackingParameters))
            )
            .andExpect(status().isOk());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
        WasteTrackingParameters testWasteTrackingParameters = wasteTrackingParametersList.get(wasteTrackingParametersList.size() - 1);
        assertThat(testWasteTrackingParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWasteTrackingParameters.getWasteIssueCreationThreshold()).isEqualTo(UPDATED_WASTE_ISSUE_CREATION_THRESHOLD);
        assertThat(testWasteTrackingParameters.getMaxWasteIssueCreationRate()).isEqualTo(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE);
        assertThat(testWasteTrackingParameters.getMaxActiveWasteIssues()).isEqualTo(UPDATED_MAX_ACTIVE_WASTE_ISSUES);
        assertThat(testWasteTrackingParameters.getAutoCreateWasteIssues()).isEqualTo(UPDATED_AUTO_CREATE_WASTE_ISSUES);
    }

    @Test
    @Transactional
    void putNonExistingWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();
        wasteTrackingParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWasteTrackingParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wasteTrackingParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();
        wasteTrackingParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWasteTrackingParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();
        wasteTrackingParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWasteTrackingParametersMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWasteTrackingParametersWithPatch() throws Exception {
        // Initialize the database
        wasteTrackingParametersRepository.saveAndFlush(wasteTrackingParameters);

        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();

        // Update the wasteTrackingParameters using partial update
        WasteTrackingParameters partialUpdatedWasteTrackingParameters = new WasteTrackingParameters();
        partialUpdatedWasteTrackingParameters.setId(wasteTrackingParameters.getId());

        partialUpdatedWasteTrackingParameters
            .name(UPDATED_NAME)
            .maxWasteIssueCreationRate(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE)
            .maxActiveWasteIssues(UPDATED_MAX_ACTIVE_WASTE_ISSUES)
            .autoCreateWasteIssues(UPDATED_AUTO_CREATE_WASTE_ISSUES);

        restWasteTrackingParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWasteTrackingParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWasteTrackingParameters))
            )
            .andExpect(status().isOk());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
        WasteTrackingParameters testWasteTrackingParameters = wasteTrackingParametersList.get(wasteTrackingParametersList.size() - 1);
        assertThat(testWasteTrackingParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWasteTrackingParameters.getWasteIssueCreationThreshold()).isEqualTo(DEFAULT_WASTE_ISSUE_CREATION_THRESHOLD);
        assertThat(testWasteTrackingParameters.getMaxWasteIssueCreationRate()).isEqualTo(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE);
        assertThat(testWasteTrackingParameters.getMaxActiveWasteIssues()).isEqualTo(UPDATED_MAX_ACTIVE_WASTE_ISSUES);
        assertThat(testWasteTrackingParameters.getAutoCreateWasteIssues()).isEqualTo(UPDATED_AUTO_CREATE_WASTE_ISSUES);
    }

    @Test
    @Transactional
    void fullUpdateWasteTrackingParametersWithPatch() throws Exception {
        // Initialize the database
        wasteTrackingParametersRepository.saveAndFlush(wasteTrackingParameters);

        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();

        // Update the wasteTrackingParameters using partial update
        WasteTrackingParameters partialUpdatedWasteTrackingParameters = new WasteTrackingParameters();
        partialUpdatedWasteTrackingParameters.setId(wasteTrackingParameters.getId());

        partialUpdatedWasteTrackingParameters
            .name(UPDATED_NAME)
            .wasteIssueCreationThreshold(UPDATED_WASTE_ISSUE_CREATION_THRESHOLD)
            .maxWasteIssueCreationRate(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE)
            .maxActiveWasteIssues(UPDATED_MAX_ACTIVE_WASTE_ISSUES)
            .autoCreateWasteIssues(UPDATED_AUTO_CREATE_WASTE_ISSUES);

        restWasteTrackingParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWasteTrackingParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWasteTrackingParameters))
            )
            .andExpect(status().isOk());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
        WasteTrackingParameters testWasteTrackingParameters = wasteTrackingParametersList.get(wasteTrackingParametersList.size() - 1);
        assertThat(testWasteTrackingParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWasteTrackingParameters.getWasteIssueCreationThreshold()).isEqualTo(UPDATED_WASTE_ISSUE_CREATION_THRESHOLD);
        assertThat(testWasteTrackingParameters.getMaxWasteIssueCreationRate()).isEqualTo(UPDATED_MAX_WASTE_ISSUE_CREATION_RATE);
        assertThat(testWasteTrackingParameters.getMaxActiveWasteIssues()).isEqualTo(UPDATED_MAX_ACTIVE_WASTE_ISSUES);
        assertThat(testWasteTrackingParameters.getAutoCreateWasteIssues()).isEqualTo(UPDATED_AUTO_CREATE_WASTE_ISSUES);
    }

    @Test
    @Transactional
    void patchNonExistingWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();
        wasteTrackingParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWasteTrackingParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wasteTrackingParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();
        wasteTrackingParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWasteTrackingParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWasteTrackingParameters() throws Exception {
        int databaseSizeBeforeUpdate = wasteTrackingParametersRepository.findAll().size();
        wasteTrackingParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWasteTrackingParametersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wasteTrackingParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WasteTrackingParameters in the database
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWasteTrackingParameters() throws Exception {
        // Initialize the database
        wasteTrackingParametersRepository.saveAndFlush(wasteTrackingParameters);

        int databaseSizeBeforeDelete = wasteTrackingParametersRepository.findAll().size();

        // Delete the wasteTrackingParameters
        restWasteTrackingParametersMockMvc
            .perform(delete(ENTITY_API_URL_ID, wasteTrackingParameters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WasteTrackingParameters> wasteTrackingParametersList = wasteTrackingParametersRepository.findAll();
        assertThat(wasteTrackingParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
