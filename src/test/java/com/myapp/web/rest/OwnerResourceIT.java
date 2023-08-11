package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Owner;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.domain.enumeration.Stage;
import com.myapp.repository.OwnerRepository;
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
 * Integration tests for the {@link OwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_METERS = 1;
    private static final Integer UPDATED_METERS = 2;

    private static final Integer DEFAULT_LAST_WEEK = 1;
    private static final Integer UPDATED_LAST_WEEK = 2;

    private static final Integer DEFAULT_BEFORE_LAST_WEEK = 1;
    private static final Integer UPDATED_BEFORE_LAST_WEEK = 2;

    private static final Integer DEFAULT_AMR = 1;
    private static final Integer UPDATED_AMR = 2;

    private static final Integer DEFAULT_LAST_YEAR = 1;
    private static final Integer UPDATED_LAST_YEAR = 2;

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final Float DEFAULT_ELECTRICITY_PRICE = 1F;
    private static final Float UPDATED_ELECTRICITY_PRICE = 2F;

    private static final Float DEFAULT_GAS_PRICE = 1F;
    private static final Float UPDATED_GAS_PRICE = 2F;

    private static final Stage DEFAULT_GAS_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_GAS_STAGE = Stage.METERS_LISTED;

    private static final Stage DEFAULT_ELECTRICITY_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_ELECTRICITY_STAGE = Stage.METERS_LISTED;

    private static final Stage DEFAULT_WATER_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_WATER_STAGE = Stage.METERS_LISTED;

    private static final Stage DEFAULT_HEAT_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_HEAT_STAGE = Stage.METERS_LISTED;

    private static final Stage DEFAULT_SOLAR_HEAT = Stage.PROSPECT;
    private static final Stage UPDATED_SOLAR_HEAT = Stage.METERS_LISTED;

    private static final Stage DEFAULT_SOLAR_POWER_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_SOLAR_POWER_STAGE = Stage.METERS_LISTED;

    private static final Stage DEFAULT_WIND_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_WIND_STAGE = Stage.METERS_LISTED;

    private static final Stage DEFAULT_COGEN_POWER_STAGE = Stage.PROSPECT;
    private static final Stage UPDATED_COGEN_POWER_STAGE = Stage.METERS_LISTED;

    private static final String ENTITY_API_URL = "/api/owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnerMockMvc;

    private Owner owner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createEntity(EntityManager em) {
        Owner owner = new Owner()
            .name(DEFAULT_NAME)
            .fullName(DEFAULT_FULL_NAME)
            .ownerKey(DEFAULT_OWNER_KEY)
            .ownerGroup(DEFAULT_OWNER_GROUP)
            .meters(DEFAULT_METERS)
            .lastWeek(DEFAULT_LAST_WEEK)
            .beforeLastWeek(DEFAULT_BEFORE_LAST_WEEK)
            .amr(DEFAULT_AMR)
            .lastYear(DEFAULT_LAST_YEAR)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .electricityPrice(DEFAULT_ELECTRICITY_PRICE)
            .gasPrice(DEFAULT_GAS_PRICE)
            .gasStage(DEFAULT_GAS_STAGE)
            .electricityStage(DEFAULT_ELECTRICITY_STAGE)
            .waterStage(DEFAULT_WATER_STAGE)
            .heatStage(DEFAULT_HEAT_STAGE)
            .solarHeat(DEFAULT_SOLAR_HEAT)
            .solarPowerStage(DEFAULT_SOLAR_POWER_STAGE)
            .windStage(DEFAULT_WIND_STAGE)
            .cogenPowerStage(DEFAULT_COGEN_POWER_STAGE);
        return owner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createUpdatedEntity(EntityManager em) {
        Owner owner = new Owner()
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .ownerKey(UPDATED_OWNER_KEY)
            .ownerGroup(UPDATED_OWNER_GROUP)
            .meters(UPDATED_METERS)
            .lastWeek(UPDATED_LAST_WEEK)
            .beforeLastWeek(UPDATED_BEFORE_LAST_WEEK)
            .amr(UPDATED_AMR)
            .lastYear(UPDATED_LAST_YEAR)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .electricityPrice(UPDATED_ELECTRICITY_PRICE)
            .gasPrice(UPDATED_GAS_PRICE)
            .gasStage(UPDATED_GAS_STAGE)
            .electricityStage(UPDATED_ELECTRICITY_STAGE)
            .waterStage(UPDATED_WATER_STAGE)
            .heatStage(UPDATED_HEAT_STAGE)
            .solarHeat(UPDATED_SOLAR_HEAT)
            .solarPowerStage(UPDATED_SOLAR_POWER_STAGE)
            .windStage(UPDATED_WIND_STAGE)
            .cogenPowerStage(UPDATED_COGEN_POWER_STAGE);
        return owner;
    }

    @BeforeEach
    public void initTest() {
        owner = createEntity(em);
    }

    @Test
    @Transactional
    void createOwner() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();
        // Create the Owner
        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isCreated());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate + 1);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOwner.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testOwner.getOwnerKey()).isEqualTo(DEFAULT_OWNER_KEY);
        assertThat(testOwner.getOwnerGroup()).isEqualTo(DEFAULT_OWNER_GROUP);
        assertThat(testOwner.getMeters()).isEqualTo(DEFAULT_METERS);
        assertThat(testOwner.getLastWeek()).isEqualTo(DEFAULT_LAST_WEEK);
        assertThat(testOwner.getBeforeLastWeek()).isEqualTo(DEFAULT_BEFORE_LAST_WEEK);
        assertThat(testOwner.getAmr()).isEqualTo(DEFAULT_AMR);
        assertThat(testOwner.getLastYear()).isEqualTo(DEFAULT_LAST_YEAR);
        assertThat(testOwner.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testOwner.getElectricityPrice()).isEqualTo(DEFAULT_ELECTRICITY_PRICE);
        assertThat(testOwner.getGasPrice()).isEqualTo(DEFAULT_GAS_PRICE);
        assertThat(testOwner.getGasStage()).isEqualTo(DEFAULT_GAS_STAGE);
        assertThat(testOwner.getElectricityStage()).isEqualTo(DEFAULT_ELECTRICITY_STAGE);
        assertThat(testOwner.getWaterStage()).isEqualTo(DEFAULT_WATER_STAGE);
        assertThat(testOwner.getHeatStage()).isEqualTo(DEFAULT_HEAT_STAGE);
        assertThat(testOwner.getSolarHeat()).isEqualTo(DEFAULT_SOLAR_HEAT);
        assertThat(testOwner.getSolarPowerStage()).isEqualTo(DEFAULT_SOLAR_POWER_STAGE);
        assertThat(testOwner.getWindStage()).isEqualTo(DEFAULT_WIND_STAGE);
        assertThat(testOwner.getCogenPowerStage()).isEqualTo(DEFAULT_COGEN_POWER_STAGE);
    }

    @Test
    @Transactional
    void createOwnerWithExistingId() throws Exception {
        // Create the Owner with an existing ID
        owner.setId(1L);

        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setName(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].ownerKey").value(hasItem(DEFAULT_OWNER_KEY)))
            .andExpect(jsonPath("$.[*].ownerGroup").value(hasItem(DEFAULT_OWNER_GROUP)))
            .andExpect(jsonPath("$.[*].meters").value(hasItem(DEFAULT_METERS)))
            .andExpect(jsonPath("$.[*].lastWeek").value(hasItem(DEFAULT_LAST_WEEK)))
            .andExpect(jsonPath("$.[*].beforeLastWeek").value(hasItem(DEFAULT_BEFORE_LAST_WEEK)))
            .andExpect(jsonPath("$.[*].amr").value(hasItem(DEFAULT_AMR)))
            .andExpect(jsonPath("$.[*].lastYear").value(hasItem(DEFAULT_LAST_YEAR)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].electricityPrice").value(hasItem(DEFAULT_ELECTRICITY_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].gasPrice").value(hasItem(DEFAULT_GAS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].gasStage").value(hasItem(DEFAULT_GAS_STAGE.toString())))
            .andExpect(jsonPath("$.[*].electricityStage").value(hasItem(DEFAULT_ELECTRICITY_STAGE.toString())))
            .andExpect(jsonPath("$.[*].waterStage").value(hasItem(DEFAULT_WATER_STAGE.toString())))
            .andExpect(jsonPath("$.[*].heatStage").value(hasItem(DEFAULT_HEAT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].solarHeat").value(hasItem(DEFAULT_SOLAR_HEAT.toString())))
            .andExpect(jsonPath("$.[*].solarPowerStage").value(hasItem(DEFAULT_SOLAR_POWER_STAGE.toString())))
            .andExpect(jsonPath("$.[*].windStage").value(hasItem(DEFAULT_WIND_STAGE.toString())))
            .andExpect(jsonPath("$.[*].cogenPowerStage").value(hasItem(DEFAULT_COGEN_POWER_STAGE.toString())));
    }

    @Test
    @Transactional
    void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.ownerKey").value(DEFAULT_OWNER_KEY))
            .andExpect(jsonPath("$.ownerGroup").value(DEFAULT_OWNER_GROUP))
            .andExpect(jsonPath("$.meters").value(DEFAULT_METERS))
            .andExpect(jsonPath("$.lastWeek").value(DEFAULT_LAST_WEEK))
            .andExpect(jsonPath("$.beforeLastWeek").value(DEFAULT_BEFORE_LAST_WEEK))
            .andExpect(jsonPath("$.amr").value(DEFAULT_AMR))
            .andExpect(jsonPath("$.lastYear").value(DEFAULT_LAST_YEAR))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.electricityPrice").value(DEFAULT_ELECTRICITY_PRICE.doubleValue()))
            .andExpect(jsonPath("$.gasPrice").value(DEFAULT_GAS_PRICE.doubleValue()))
            .andExpect(jsonPath("$.gasStage").value(DEFAULT_GAS_STAGE.toString()))
            .andExpect(jsonPath("$.electricityStage").value(DEFAULT_ELECTRICITY_STAGE.toString()))
            .andExpect(jsonPath("$.waterStage").value(DEFAULT_WATER_STAGE.toString()))
            .andExpect(jsonPath("$.heatStage").value(DEFAULT_HEAT_STAGE.toString()))
            .andExpect(jsonPath("$.solarHeat").value(DEFAULT_SOLAR_HEAT.toString()))
            .andExpect(jsonPath("$.solarPowerStage").value(DEFAULT_SOLAR_POWER_STAGE.toString()))
            .andExpect(jsonPath("$.windStage").value(DEFAULT_WIND_STAGE.toString()))
            .andExpect(jsonPath("$.cogenPowerStage").value(DEFAULT_COGEN_POWER_STAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOwner are not directly saved in db
        em.detach(updatedOwner);
        updatedOwner
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .ownerKey(UPDATED_OWNER_KEY)
            .ownerGroup(UPDATED_OWNER_GROUP)
            .meters(UPDATED_METERS)
            .lastWeek(UPDATED_LAST_WEEK)
            .beforeLastWeek(UPDATED_BEFORE_LAST_WEEK)
            .amr(UPDATED_AMR)
            .lastYear(UPDATED_LAST_YEAR)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .electricityPrice(UPDATED_ELECTRICITY_PRICE)
            .gasPrice(UPDATED_GAS_PRICE)
            .gasStage(UPDATED_GAS_STAGE)
            .electricityStage(UPDATED_ELECTRICITY_STAGE)
            .waterStage(UPDATED_WATER_STAGE)
            .heatStage(UPDATED_HEAT_STAGE)
            .solarHeat(UPDATED_SOLAR_HEAT)
            .solarPowerStage(UPDATED_SOLAR_POWER_STAGE)
            .windStage(UPDATED_WIND_STAGE)
            .cogenPowerStage(UPDATED_COGEN_POWER_STAGE);

        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOwner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwner.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testOwner.getOwnerKey()).isEqualTo(UPDATED_OWNER_KEY);
        assertThat(testOwner.getOwnerGroup()).isEqualTo(UPDATED_OWNER_GROUP);
        assertThat(testOwner.getMeters()).isEqualTo(UPDATED_METERS);
        assertThat(testOwner.getLastWeek()).isEqualTo(UPDATED_LAST_WEEK);
        assertThat(testOwner.getBeforeLastWeek()).isEqualTo(UPDATED_BEFORE_LAST_WEEK);
        assertThat(testOwner.getAmr()).isEqualTo(UPDATED_AMR);
        assertThat(testOwner.getLastYear()).isEqualTo(UPDATED_LAST_YEAR);
        assertThat(testOwner.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testOwner.getElectricityPrice()).isEqualTo(UPDATED_ELECTRICITY_PRICE);
        assertThat(testOwner.getGasPrice()).isEqualTo(UPDATED_GAS_PRICE);
        assertThat(testOwner.getGasStage()).isEqualTo(UPDATED_GAS_STAGE);
        assertThat(testOwner.getElectricityStage()).isEqualTo(UPDATED_ELECTRICITY_STAGE);
        assertThat(testOwner.getWaterStage()).isEqualTo(UPDATED_WATER_STAGE);
        assertThat(testOwner.getHeatStage()).isEqualTo(UPDATED_HEAT_STAGE);
        assertThat(testOwner.getSolarHeat()).isEqualTo(UPDATED_SOLAR_HEAT);
        assertThat(testOwner.getSolarPowerStage()).isEqualTo(UPDATED_SOLAR_POWER_STAGE);
        assertThat(testOwner.getWindStage()).isEqualTo(UPDATED_WIND_STAGE);
        assertThat(testOwner.getCogenPowerStage()).isEqualTo(UPDATED_COGEN_POWER_STAGE);
    }

    @Test
    @Transactional
    void putNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, owner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .fullName(UPDATED_FULL_NAME)
            .ownerKey(UPDATED_OWNER_KEY)
            .meters(UPDATED_METERS)
            .lastWeek(UPDATED_LAST_WEEK)
            .beforeLastWeek(UPDATED_BEFORE_LAST_WEEK)
            .amr(UPDATED_AMR)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .gasPrice(UPDATED_GAS_PRICE)
            .heatStage(UPDATED_HEAT_STAGE)
            .windStage(UPDATED_WIND_STAGE);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOwner.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testOwner.getOwnerKey()).isEqualTo(UPDATED_OWNER_KEY);
        assertThat(testOwner.getOwnerGroup()).isEqualTo(DEFAULT_OWNER_GROUP);
        assertThat(testOwner.getMeters()).isEqualTo(UPDATED_METERS);
        assertThat(testOwner.getLastWeek()).isEqualTo(UPDATED_LAST_WEEK);
        assertThat(testOwner.getBeforeLastWeek()).isEqualTo(UPDATED_BEFORE_LAST_WEEK);
        assertThat(testOwner.getAmr()).isEqualTo(UPDATED_AMR);
        assertThat(testOwner.getLastYear()).isEqualTo(DEFAULT_LAST_YEAR);
        assertThat(testOwner.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testOwner.getElectricityPrice()).isEqualTo(DEFAULT_ELECTRICITY_PRICE);
        assertThat(testOwner.getGasPrice()).isEqualTo(UPDATED_GAS_PRICE);
        assertThat(testOwner.getGasStage()).isEqualTo(DEFAULT_GAS_STAGE);
        assertThat(testOwner.getElectricityStage()).isEqualTo(DEFAULT_ELECTRICITY_STAGE);
        assertThat(testOwner.getWaterStage()).isEqualTo(DEFAULT_WATER_STAGE);
        assertThat(testOwner.getHeatStage()).isEqualTo(UPDATED_HEAT_STAGE);
        assertThat(testOwner.getSolarHeat()).isEqualTo(DEFAULT_SOLAR_HEAT);
        assertThat(testOwner.getSolarPowerStage()).isEqualTo(DEFAULT_SOLAR_POWER_STAGE);
        assertThat(testOwner.getWindStage()).isEqualTo(UPDATED_WIND_STAGE);
        assertThat(testOwner.getCogenPowerStage()).isEqualTo(DEFAULT_COGEN_POWER_STAGE);
    }

    @Test
    @Transactional
    void fullUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .name(UPDATED_NAME)
            .fullName(UPDATED_FULL_NAME)
            .ownerKey(UPDATED_OWNER_KEY)
            .ownerGroup(UPDATED_OWNER_GROUP)
            .meters(UPDATED_METERS)
            .lastWeek(UPDATED_LAST_WEEK)
            .beforeLastWeek(UPDATED_BEFORE_LAST_WEEK)
            .amr(UPDATED_AMR)
            .lastYear(UPDATED_LAST_YEAR)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .electricityPrice(UPDATED_ELECTRICITY_PRICE)
            .gasPrice(UPDATED_GAS_PRICE)
            .gasStage(UPDATED_GAS_STAGE)
            .electricityStage(UPDATED_ELECTRICITY_STAGE)
            .waterStage(UPDATED_WATER_STAGE)
            .heatStage(UPDATED_HEAT_STAGE)
            .solarHeat(UPDATED_SOLAR_HEAT)
            .solarPowerStage(UPDATED_SOLAR_POWER_STAGE)
            .windStage(UPDATED_WIND_STAGE)
            .cogenPowerStage(UPDATED_COGEN_POWER_STAGE);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwner.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testOwner.getOwnerKey()).isEqualTo(UPDATED_OWNER_KEY);
        assertThat(testOwner.getOwnerGroup()).isEqualTo(UPDATED_OWNER_GROUP);
        assertThat(testOwner.getMeters()).isEqualTo(UPDATED_METERS);
        assertThat(testOwner.getLastWeek()).isEqualTo(UPDATED_LAST_WEEK);
        assertThat(testOwner.getBeforeLastWeek()).isEqualTo(UPDATED_BEFORE_LAST_WEEK);
        assertThat(testOwner.getAmr()).isEqualTo(UPDATED_AMR);
        assertThat(testOwner.getLastYear()).isEqualTo(UPDATED_LAST_YEAR);
        assertThat(testOwner.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testOwner.getElectricityPrice()).isEqualTo(UPDATED_ELECTRICITY_PRICE);
        assertThat(testOwner.getGasPrice()).isEqualTo(UPDATED_GAS_PRICE);
        assertThat(testOwner.getGasStage()).isEqualTo(UPDATED_GAS_STAGE);
        assertThat(testOwner.getElectricityStage()).isEqualTo(UPDATED_ELECTRICITY_STAGE);
        assertThat(testOwner.getWaterStage()).isEqualTo(UPDATED_WATER_STAGE);
        assertThat(testOwner.getHeatStage()).isEqualTo(UPDATED_HEAT_STAGE);
        assertThat(testOwner.getSolarHeat()).isEqualTo(UPDATED_SOLAR_HEAT);
        assertThat(testOwner.getSolarPowerStage()).isEqualTo(UPDATED_SOLAR_POWER_STAGE);
        assertThat(testOwner.getWindStage()).isEqualTo(UPDATED_WIND_STAGE);
        assertThat(testOwner.getCogenPowerStage()).isEqualTo(UPDATED_COGEN_POWER_STAGE);
    }

    @Test
    @Transactional
    void patchNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, owner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Delete the owner
        restOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, owner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
