package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Scope;
import com.myapp.repository.ScopeRepository;
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
 * Integration tests for the {@link ScopeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScopeResourceIT {

    private static final String DEFAULT_METER_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_METER_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_METER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_METER_UTILITY = "AAAAAAAAAA";
    private static final String UPDATED_METER_UTILITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE_PER_MONTH = 1;
    private static final Integer UPDATED_PRICE_PER_MONTH = 2;

    private static final String ENTITY_API_URL = "/api/scopes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScopeMockMvc;

    private Scope scope;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scope createEntity(EntityManager em) {
        Scope scope = new Scope()
            .meterDescription(DEFAULT_METER_DESCRIPTION)
            .meterName(DEFAULT_METER_NAME)
            .meterUtility(DEFAULT_METER_UTILITY)
            .pricePerMonth(DEFAULT_PRICE_PER_MONTH);
        return scope;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scope createUpdatedEntity(EntityManager em) {
        Scope scope = new Scope()
            .meterDescription(UPDATED_METER_DESCRIPTION)
            .meterName(UPDATED_METER_NAME)
            .meterUtility(UPDATED_METER_UTILITY)
            .pricePerMonth(UPDATED_PRICE_PER_MONTH);
        return scope;
    }

    @BeforeEach
    public void initTest() {
        scope = createEntity(em);
    }

    @Test
    @Transactional
    void createScope() throws Exception {
        int databaseSizeBeforeCreate = scopeRepository.findAll().size();
        // Create the Scope
        restScopeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scope)))
            .andExpect(status().isCreated());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeCreate + 1);
        Scope testScope = scopeList.get(scopeList.size() - 1);
        assertThat(testScope.getMeterDescription()).isEqualTo(DEFAULT_METER_DESCRIPTION);
        assertThat(testScope.getMeterName()).isEqualTo(DEFAULT_METER_NAME);
        assertThat(testScope.getMeterUtility()).isEqualTo(DEFAULT_METER_UTILITY);
        assertThat(testScope.getPricePerMonth()).isEqualTo(DEFAULT_PRICE_PER_MONTH);
    }

    @Test
    @Transactional
    void createScopeWithExistingId() throws Exception {
        // Create the Scope with an existing ID
        scope.setId(1L);

        int databaseSizeBeforeCreate = scopeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScopeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scope)))
            .andExpect(status().isBadRequest());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScopes() throws Exception {
        // Initialize the database
        scopeRepository.saveAndFlush(scope);

        // Get all the scopeList
        restScopeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scope.getId().intValue())))
            .andExpect(jsonPath("$.[*].meterDescription").value(hasItem(DEFAULT_METER_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].meterName").value(hasItem(DEFAULT_METER_NAME)))
            .andExpect(jsonPath("$.[*].meterUtility").value(hasItem(DEFAULT_METER_UTILITY)))
            .andExpect(jsonPath("$.[*].pricePerMonth").value(hasItem(DEFAULT_PRICE_PER_MONTH)));
    }

    @Test
    @Transactional
    void getScope() throws Exception {
        // Initialize the database
        scopeRepository.saveAndFlush(scope);

        // Get the scope
        restScopeMockMvc
            .perform(get(ENTITY_API_URL_ID, scope.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scope.getId().intValue()))
            .andExpect(jsonPath("$.meterDescription").value(DEFAULT_METER_DESCRIPTION))
            .andExpect(jsonPath("$.meterName").value(DEFAULT_METER_NAME))
            .andExpect(jsonPath("$.meterUtility").value(DEFAULT_METER_UTILITY))
            .andExpect(jsonPath("$.pricePerMonth").value(DEFAULT_PRICE_PER_MONTH));
    }

    @Test
    @Transactional
    void getNonExistingScope() throws Exception {
        // Get the scope
        restScopeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScope() throws Exception {
        // Initialize the database
        scopeRepository.saveAndFlush(scope);

        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();

        // Update the scope
        Scope updatedScope = scopeRepository.findById(scope.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScope are not directly saved in db
        em.detach(updatedScope);
        updatedScope
            .meterDescription(UPDATED_METER_DESCRIPTION)
            .meterName(UPDATED_METER_NAME)
            .meterUtility(UPDATED_METER_UTILITY)
            .pricePerMonth(UPDATED_PRICE_PER_MONTH);

        restScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScope.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScope))
            )
            .andExpect(status().isOk());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
        Scope testScope = scopeList.get(scopeList.size() - 1);
        assertThat(testScope.getMeterDescription()).isEqualTo(UPDATED_METER_DESCRIPTION);
        assertThat(testScope.getMeterName()).isEqualTo(UPDATED_METER_NAME);
        assertThat(testScope.getMeterUtility()).isEqualTo(UPDATED_METER_UTILITY);
        assertThat(testScope.getPricePerMonth()).isEqualTo(UPDATED_PRICE_PER_MONTH);
    }

    @Test
    @Transactional
    void putNonExistingScope() throws Exception {
        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();
        scope.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scope.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scope))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScope() throws Exception {
        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();
        scope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scope))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScope() throws Exception {
        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();
        scope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScopeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scope)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScopeWithPatch() throws Exception {
        // Initialize the database
        scopeRepository.saveAndFlush(scope);

        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();

        // Update the scope using partial update
        Scope partialUpdatedScope = new Scope();
        partialUpdatedScope.setId(scope.getId());

        partialUpdatedScope.meterName(UPDATED_METER_NAME);

        restScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScope))
            )
            .andExpect(status().isOk());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
        Scope testScope = scopeList.get(scopeList.size() - 1);
        assertThat(testScope.getMeterDescription()).isEqualTo(DEFAULT_METER_DESCRIPTION);
        assertThat(testScope.getMeterName()).isEqualTo(UPDATED_METER_NAME);
        assertThat(testScope.getMeterUtility()).isEqualTo(DEFAULT_METER_UTILITY);
        assertThat(testScope.getPricePerMonth()).isEqualTo(DEFAULT_PRICE_PER_MONTH);
    }

    @Test
    @Transactional
    void fullUpdateScopeWithPatch() throws Exception {
        // Initialize the database
        scopeRepository.saveAndFlush(scope);

        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();

        // Update the scope using partial update
        Scope partialUpdatedScope = new Scope();
        partialUpdatedScope.setId(scope.getId());

        partialUpdatedScope
            .meterDescription(UPDATED_METER_DESCRIPTION)
            .meterName(UPDATED_METER_NAME)
            .meterUtility(UPDATED_METER_UTILITY)
            .pricePerMonth(UPDATED_PRICE_PER_MONTH);

        restScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScope))
            )
            .andExpect(status().isOk());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
        Scope testScope = scopeList.get(scopeList.size() - 1);
        assertThat(testScope.getMeterDescription()).isEqualTo(UPDATED_METER_DESCRIPTION);
        assertThat(testScope.getMeterName()).isEqualTo(UPDATED_METER_NAME);
        assertThat(testScope.getMeterUtility()).isEqualTo(UPDATED_METER_UTILITY);
        assertThat(testScope.getPricePerMonth()).isEqualTo(UPDATED_PRICE_PER_MONTH);
    }

    @Test
    @Transactional
    void patchNonExistingScope() throws Exception {
        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();
        scope.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scope))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScope() throws Exception {
        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();
        scope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scope))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScope() throws Exception {
        int databaseSizeBeforeUpdate = scopeRepository.findAll().size();
        scope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScopeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(scope)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scope in the database
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScope() throws Exception {
        // Initialize the database
        scopeRepository.saveAndFlush(scope);

        int databaseSizeBeforeDelete = scopeRepository.findAll().size();

        // Delete the scope
        restScopeMockMvc
            .perform(delete(ENTITY_API_URL_ID, scope.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scope> scopeList = scopeRepository.findAll();
        assertThat(scopeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
