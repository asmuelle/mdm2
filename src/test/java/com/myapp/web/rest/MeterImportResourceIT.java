package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.MeterImport;
import com.myapp.domain.enumeration.Utility;
import com.myapp.repository.MeterImportRepository;
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
 * Integration tests for the {@link MeterImportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeterImportResourceIT {

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final Utility DEFAULT_UTILITY = Utility.GAS;
    private static final Utility UPDATED_UTILITY = Utility.ELECTRICITY;

    private static final String DEFAULT_NAMESPACE = "AAAAAAAAAA";
    private static final String UPDATED_NAMESPACE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_REF = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_REF = "BBBBBBBBBB";

    private static final String DEFAULT_METER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OWNERSHIP = "AAAAAAAAAA";
    private static final String UPDATED_OWNERSHIP = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_POSTCODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSLINES = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSLINES = "BBBBBBBBBB";

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;

    private static final Float DEFAULT_LON = 1F;
    private static final Float UPDATED_LON = 2F;

    private static final String DEFAULT_CLASSIFICATIONS = "AAAAAAAAAA";
    private static final String UPDATED_CLASSIFICATIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meter-imports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeterImportRepository meterImportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeterImportMockMvc;

    private MeterImport meterImport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeterImport createEntity(EntityManager em) {
        MeterImport meterImport = new MeterImport()
            .provider(DEFAULT_PROVIDER)
            .utility(DEFAULT_UTILITY)
            .namespace(DEFAULT_NAMESPACE)
            .clientRef(DEFAULT_CLIENT_REF)
            .meterName(DEFAULT_METER_NAME)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .ownership(DEFAULT_OWNERSHIP)
            .owner(DEFAULT_OWNER)
            .postcode(DEFAULT_POSTCODE)
            .addresslines(DEFAULT_ADDRESSLINES)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .classifications(DEFAULT_CLASSIFICATIONS);
        return meterImport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeterImport createUpdatedEntity(EntityManager em) {
        MeterImport meterImport = new MeterImport()
            .provider(UPDATED_PROVIDER)
            .utility(UPDATED_UTILITY)
            .namespace(UPDATED_NAMESPACE)
            .clientRef(UPDATED_CLIENT_REF)
            .meterName(UPDATED_METER_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .ownership(UPDATED_OWNERSHIP)
            .owner(UPDATED_OWNER)
            .postcode(UPDATED_POSTCODE)
            .addresslines(UPDATED_ADDRESSLINES)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .classifications(UPDATED_CLASSIFICATIONS);
        return meterImport;
    }

    @BeforeEach
    public void initTest() {
        meterImport = createEntity(em);
    }

    @Test
    @Transactional
    void createMeterImport() throws Exception {
        int databaseSizeBeforeCreate = meterImportRepository.findAll().size();
        // Create the MeterImport
        restMeterImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meterImport)))
            .andExpect(status().isCreated());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeCreate + 1);
        MeterImport testMeterImport = meterImportList.get(meterImportList.size() - 1);
        assertThat(testMeterImport.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testMeterImport.getUtility()).isEqualTo(DEFAULT_UTILITY);
        assertThat(testMeterImport.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testMeterImport.getClientRef()).isEqualTo(DEFAULT_CLIENT_REF);
        assertThat(testMeterImport.getMeterName()).isEqualTo(DEFAULT_METER_NAME);
        assertThat(testMeterImport.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testMeterImport.getOwnership()).isEqualTo(DEFAULT_OWNERSHIP);
        assertThat(testMeterImport.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testMeterImport.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testMeterImport.getAddresslines()).isEqualTo(DEFAULT_ADDRESSLINES);
        assertThat(testMeterImport.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testMeterImport.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testMeterImport.getClassifications()).isEqualTo(DEFAULT_CLASSIFICATIONS);
    }

    @Test
    @Transactional
    void createMeterImportWithExistingId() throws Exception {
        // Create the MeterImport with an existing ID
        meterImport.setId(1L);

        int databaseSizeBeforeCreate = meterImportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeterImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meterImport)))
            .andExpect(status().isBadRequest());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeterImports() throws Exception {
        // Initialize the database
        meterImportRepository.saveAndFlush(meterImport);

        // Get all the meterImportList
        restMeterImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meterImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.toString())))
            .andExpect(jsonPath("$.[*].namespace").value(hasItem(DEFAULT_NAMESPACE)))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].meterName").value(hasItem(DEFAULT_METER_NAME)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].ownership").value(hasItem(DEFAULT_OWNERSHIP)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].addresslines").value(hasItem(DEFAULT_ADDRESSLINES)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].classifications").value(hasItem(DEFAULT_CLASSIFICATIONS)));
    }

    @Test
    @Transactional
    void getMeterImport() throws Exception {
        // Initialize the database
        meterImportRepository.saveAndFlush(meterImport);

        // Get the meterImport
        restMeterImportMockMvc
            .perform(get(ENTITY_API_URL_ID, meterImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meterImport.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER))
            .andExpect(jsonPath("$.utility").value(DEFAULT_UTILITY.toString()))
            .andExpect(jsonPath("$.namespace").value(DEFAULT_NAMESPACE))
            .andExpect(jsonPath("$.clientRef").value(DEFAULT_CLIENT_REF))
            .andExpect(jsonPath("$.meterName").value(DEFAULT_METER_NAME))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.ownership").value(DEFAULT_OWNERSHIP))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE))
            .andExpect(jsonPath("$.addresslines").value(DEFAULT_ADDRESSLINES))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.classifications").value(DEFAULT_CLASSIFICATIONS));
    }

    @Test
    @Transactional
    void getNonExistingMeterImport() throws Exception {
        // Get the meterImport
        restMeterImportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeterImport() throws Exception {
        // Initialize the database
        meterImportRepository.saveAndFlush(meterImport);

        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();

        // Update the meterImport
        MeterImport updatedMeterImport = meterImportRepository.findById(meterImport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMeterImport are not directly saved in db
        em.detach(updatedMeterImport);
        updatedMeterImport
            .provider(UPDATED_PROVIDER)
            .utility(UPDATED_UTILITY)
            .namespace(UPDATED_NAMESPACE)
            .clientRef(UPDATED_CLIENT_REF)
            .meterName(UPDATED_METER_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .ownership(UPDATED_OWNERSHIP)
            .owner(UPDATED_OWNER)
            .postcode(UPDATED_POSTCODE)
            .addresslines(UPDATED_ADDRESSLINES)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .classifications(UPDATED_CLASSIFICATIONS);

        restMeterImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeterImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeterImport))
            )
            .andExpect(status().isOk());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
        MeterImport testMeterImport = meterImportList.get(meterImportList.size() - 1);
        assertThat(testMeterImport.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testMeterImport.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testMeterImport.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testMeterImport.getClientRef()).isEqualTo(UPDATED_CLIENT_REF);
        assertThat(testMeterImport.getMeterName()).isEqualTo(UPDATED_METER_NAME);
        assertThat(testMeterImport.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testMeterImport.getOwnership()).isEqualTo(UPDATED_OWNERSHIP);
        assertThat(testMeterImport.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testMeterImport.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testMeterImport.getAddresslines()).isEqualTo(UPDATED_ADDRESSLINES);
        assertThat(testMeterImport.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testMeterImport.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testMeterImport.getClassifications()).isEqualTo(UPDATED_CLASSIFICATIONS);
    }

    @Test
    @Transactional
    void putNonExistingMeterImport() throws Exception {
        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();
        meterImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeterImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meterImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meterImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeterImport() throws Exception {
        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();
        meterImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meterImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeterImport() throws Exception {
        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();
        meterImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterImportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meterImport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeterImportWithPatch() throws Exception {
        // Initialize the database
        meterImportRepository.saveAndFlush(meterImport);

        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();

        // Update the meterImport using partial update
        MeterImport partialUpdatedMeterImport = new MeterImport();
        partialUpdatedMeterImport.setId(meterImport.getId());

        partialUpdatedMeterImport
            .clientRef(UPDATED_CLIENT_REF)
            .meterName(UPDATED_METER_NAME)
            .owner(UPDATED_OWNER)
            .postcode(UPDATED_POSTCODE)
            .addresslines(UPDATED_ADDRESSLINES);

        restMeterImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeterImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeterImport))
            )
            .andExpect(status().isOk());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
        MeterImport testMeterImport = meterImportList.get(meterImportList.size() - 1);
        assertThat(testMeterImport.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testMeterImport.getUtility()).isEqualTo(DEFAULT_UTILITY);
        assertThat(testMeterImport.getNamespace()).isEqualTo(DEFAULT_NAMESPACE);
        assertThat(testMeterImport.getClientRef()).isEqualTo(UPDATED_CLIENT_REF);
        assertThat(testMeterImport.getMeterName()).isEqualTo(UPDATED_METER_NAME);
        assertThat(testMeterImport.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testMeterImport.getOwnership()).isEqualTo(DEFAULT_OWNERSHIP);
        assertThat(testMeterImport.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testMeterImport.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testMeterImport.getAddresslines()).isEqualTo(UPDATED_ADDRESSLINES);
        assertThat(testMeterImport.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testMeterImport.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testMeterImport.getClassifications()).isEqualTo(DEFAULT_CLASSIFICATIONS);
    }

    @Test
    @Transactional
    void fullUpdateMeterImportWithPatch() throws Exception {
        // Initialize the database
        meterImportRepository.saveAndFlush(meterImport);

        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();

        // Update the meterImport using partial update
        MeterImport partialUpdatedMeterImport = new MeterImport();
        partialUpdatedMeterImport.setId(meterImport.getId());

        partialUpdatedMeterImport
            .provider(UPDATED_PROVIDER)
            .utility(UPDATED_UTILITY)
            .namespace(UPDATED_NAMESPACE)
            .clientRef(UPDATED_CLIENT_REF)
            .meterName(UPDATED_METER_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .ownership(UPDATED_OWNERSHIP)
            .owner(UPDATED_OWNER)
            .postcode(UPDATED_POSTCODE)
            .addresslines(UPDATED_ADDRESSLINES)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .classifications(UPDATED_CLASSIFICATIONS);

        restMeterImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeterImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeterImport))
            )
            .andExpect(status().isOk());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
        MeterImport testMeterImport = meterImportList.get(meterImportList.size() - 1);
        assertThat(testMeterImport.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testMeterImport.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testMeterImport.getNamespace()).isEqualTo(UPDATED_NAMESPACE);
        assertThat(testMeterImport.getClientRef()).isEqualTo(UPDATED_CLIENT_REF);
        assertThat(testMeterImport.getMeterName()).isEqualTo(UPDATED_METER_NAME);
        assertThat(testMeterImport.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testMeterImport.getOwnership()).isEqualTo(UPDATED_OWNERSHIP);
        assertThat(testMeterImport.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testMeterImport.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testMeterImport.getAddresslines()).isEqualTo(UPDATED_ADDRESSLINES);
        assertThat(testMeterImport.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testMeterImport.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testMeterImport.getClassifications()).isEqualTo(UPDATED_CLASSIFICATIONS);
    }

    @Test
    @Transactional
    void patchNonExistingMeterImport() throws Exception {
        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();
        meterImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeterImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meterImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meterImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeterImport() throws Exception {
        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();
        meterImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meterImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeterImport() throws Exception {
        int databaseSizeBeforeUpdate = meterImportRepository.findAll().size();
        meterImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterImportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meterImport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeterImport in the database
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeterImport() throws Exception {
        // Initialize the database
        meterImportRepository.saveAndFlush(meterImport);

        int databaseSizeBeforeDelete = meterImportRepository.findAll().size();

        // Delete the meterImport
        restMeterImportMockMvc
            .perform(delete(ENTITY_API_URL_ID, meterImport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeterImport> meterImportList = meterImportRepository.findAll();
        assertThat(meterImportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
