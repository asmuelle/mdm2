package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Meter;
import com.myapp.domain.Meter;
import com.myapp.domain.Namespace;
import com.myapp.domain.Ownership;
import com.myapp.domain.Peer;
import com.myapp.domain.Provider;
import com.myapp.domain.enumeration.LoadType;
import com.myapp.domain.enumeration.Utility;
import com.myapp.repository.MeterRepository;
import com.myapp.service.criteria.MeterCriteria;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MeterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMR_WEEK = 1;
    private static final Integer UPDATED_AMR_WEEK = 2;
    private static final Integer SMALLER_AMR_WEEK = 1 - 1;

    private static final Integer DEFAULT_AMR_YEAR = 1;
    private static final Integer UPDATED_AMR_YEAR = 2;
    private static final Integer SMALLER_AMR_YEAR = 1 - 1;

    private static final Utility DEFAULT_UTILITY = Utility.GAS;
    private static final Utility UPDATED_UTILITY = Utility.ELECTRICITY;

    private static final LoadType DEFAULT_LOAD_TYPE = LoadType.CHILL;
    private static final LoadType UPDATED_LOAD_TYPE = LoadType.CHILL_PROCESS;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final LocalDate DEFAULT_LAST_READING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_READING = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_READING = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeterMockMvc;

    private Meter meter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meter createEntity(EntityManager em) {
        Meter meter = new Meter()
            .name(DEFAULT_NAME)
            .amrWeek(DEFAULT_AMR_WEEK)
            .amrYear(DEFAULT_AMR_YEAR)
            .utility(DEFAULT_UTILITY)
            .loadType(DEFAULT_LOAD_TYPE)
            .price(DEFAULT_PRICE)
            .lastReading(DEFAULT_LAST_READING)
            .contactEmail(DEFAULT_CONTACT_EMAIL);
        return meter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meter createUpdatedEntity(EntityManager em) {
        Meter meter = new Meter()
            .name(UPDATED_NAME)
            .amrWeek(UPDATED_AMR_WEEK)
            .amrYear(UPDATED_AMR_YEAR)
            .utility(UPDATED_UTILITY)
            .loadType(UPDATED_LOAD_TYPE)
            .price(UPDATED_PRICE)
            .lastReading(UPDATED_LAST_READING)
            .contactEmail(UPDATED_CONTACT_EMAIL);
        return meter;
    }

    @BeforeEach
    public void initTest() {
        meter = createEntity(em);
    }

    @Test
    @Transactional
    void createMeter() throws Exception {
        int databaseSizeBeforeCreate = meterRepository.findAll().size();
        // Create the Meter
        restMeterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isCreated());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeCreate + 1);
        Meter testMeter = meterList.get(meterList.size() - 1);
        assertThat(testMeter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeter.getAmrWeek()).isEqualTo(DEFAULT_AMR_WEEK);
        assertThat(testMeter.getAmrYear()).isEqualTo(DEFAULT_AMR_YEAR);
        assertThat(testMeter.getUtility()).isEqualTo(DEFAULT_UTILITY);
        assertThat(testMeter.getLoadType()).isEqualTo(DEFAULT_LOAD_TYPE);
        assertThat(testMeter.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMeter.getLastReading()).isEqualTo(DEFAULT_LAST_READING);
        assertThat(testMeter.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void createMeterWithExistingId() throws Exception {
        // Create the Meter with an existing ID
        meter.setId(1L);

        int databaseSizeBeforeCreate = meterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeters() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList
        restMeterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amrWeek").value(hasItem(DEFAULT_AMR_WEEK)))
            .andExpect(jsonPath("$.[*].amrYear").value(hasItem(DEFAULT_AMR_YEAR)))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.toString())))
            .andExpect(jsonPath("$.[*].loadType").value(hasItem(DEFAULT_LOAD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].lastReading").value(hasItem(DEFAULT_LAST_READING.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)));
    }

    @Test
    @Transactional
    void getMeter() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get the meter
        restMeterMockMvc
            .perform(get(ENTITY_API_URL_ID, meter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.amrWeek").value(DEFAULT_AMR_WEEK))
            .andExpect(jsonPath("$.amrYear").value(DEFAULT_AMR_YEAR))
            .andExpect(jsonPath("$.utility").value(DEFAULT_UTILITY.toString()))
            .andExpect(jsonPath("$.loadType").value(DEFAULT_LOAD_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.lastReading").value(DEFAULT_LAST_READING.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL));
    }

    @Test
    @Transactional
    void getMetersByIdFiltering() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        Long id = meter.getId();

        defaultMeterShouldBeFound("id.equals=" + id);
        defaultMeterShouldNotBeFound("id.notEquals=" + id);

        defaultMeterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMeterShouldNotBeFound("id.greaterThan=" + id);

        defaultMeterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMeterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where name equals to DEFAULT_NAME
        defaultMeterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the meterList where name equals to UPDATED_NAME
        defaultMeterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMeterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the meterList where name equals to UPDATED_NAME
        defaultMeterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where name is not null
        defaultMeterShouldBeFound("name.specified=true");

        // Get all the meterList where name is null
        defaultMeterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByNameContainsSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where name contains DEFAULT_NAME
        defaultMeterShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the meterList where name contains UPDATED_NAME
        defaultMeterShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where name does not contain DEFAULT_NAME
        defaultMeterShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the meterList where name does not contain UPDATED_NAME
        defaultMeterShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek equals to DEFAULT_AMR_WEEK
        defaultMeterShouldBeFound("amrWeek.equals=" + DEFAULT_AMR_WEEK);

        // Get all the meterList where amrWeek equals to UPDATED_AMR_WEEK
        defaultMeterShouldNotBeFound("amrWeek.equals=" + UPDATED_AMR_WEEK);
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek in DEFAULT_AMR_WEEK or UPDATED_AMR_WEEK
        defaultMeterShouldBeFound("amrWeek.in=" + DEFAULT_AMR_WEEK + "," + UPDATED_AMR_WEEK);

        // Get all the meterList where amrWeek equals to UPDATED_AMR_WEEK
        defaultMeterShouldNotBeFound("amrWeek.in=" + UPDATED_AMR_WEEK);
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek is not null
        defaultMeterShouldBeFound("amrWeek.specified=true");

        // Get all the meterList where amrWeek is null
        defaultMeterShouldNotBeFound("amrWeek.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek is greater than or equal to DEFAULT_AMR_WEEK
        defaultMeterShouldBeFound("amrWeek.greaterThanOrEqual=" + DEFAULT_AMR_WEEK);

        // Get all the meterList where amrWeek is greater than or equal to UPDATED_AMR_WEEK
        defaultMeterShouldNotBeFound("amrWeek.greaterThanOrEqual=" + UPDATED_AMR_WEEK);
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek is less than or equal to DEFAULT_AMR_WEEK
        defaultMeterShouldBeFound("amrWeek.lessThanOrEqual=" + DEFAULT_AMR_WEEK);

        // Get all the meterList where amrWeek is less than or equal to SMALLER_AMR_WEEK
        defaultMeterShouldNotBeFound("amrWeek.lessThanOrEqual=" + SMALLER_AMR_WEEK);
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsLessThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek is less than DEFAULT_AMR_WEEK
        defaultMeterShouldNotBeFound("amrWeek.lessThan=" + DEFAULT_AMR_WEEK);

        // Get all the meterList where amrWeek is less than UPDATED_AMR_WEEK
        defaultMeterShouldBeFound("amrWeek.lessThan=" + UPDATED_AMR_WEEK);
    }

    @Test
    @Transactional
    void getAllMetersByAmrWeekIsGreaterThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrWeek is greater than DEFAULT_AMR_WEEK
        defaultMeterShouldNotBeFound("amrWeek.greaterThan=" + DEFAULT_AMR_WEEK);

        // Get all the meterList where amrWeek is greater than SMALLER_AMR_WEEK
        defaultMeterShouldBeFound("amrWeek.greaterThan=" + SMALLER_AMR_WEEK);
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear equals to DEFAULT_AMR_YEAR
        defaultMeterShouldBeFound("amrYear.equals=" + DEFAULT_AMR_YEAR);

        // Get all the meterList where amrYear equals to UPDATED_AMR_YEAR
        defaultMeterShouldNotBeFound("amrYear.equals=" + UPDATED_AMR_YEAR);
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear in DEFAULT_AMR_YEAR or UPDATED_AMR_YEAR
        defaultMeterShouldBeFound("amrYear.in=" + DEFAULT_AMR_YEAR + "," + UPDATED_AMR_YEAR);

        // Get all the meterList where amrYear equals to UPDATED_AMR_YEAR
        defaultMeterShouldNotBeFound("amrYear.in=" + UPDATED_AMR_YEAR);
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear is not null
        defaultMeterShouldBeFound("amrYear.specified=true");

        // Get all the meterList where amrYear is null
        defaultMeterShouldNotBeFound("amrYear.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear is greater than or equal to DEFAULT_AMR_YEAR
        defaultMeterShouldBeFound("amrYear.greaterThanOrEqual=" + DEFAULT_AMR_YEAR);

        // Get all the meterList where amrYear is greater than or equal to UPDATED_AMR_YEAR
        defaultMeterShouldNotBeFound("amrYear.greaterThanOrEqual=" + UPDATED_AMR_YEAR);
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear is less than or equal to DEFAULT_AMR_YEAR
        defaultMeterShouldBeFound("amrYear.lessThanOrEqual=" + DEFAULT_AMR_YEAR);

        // Get all the meterList where amrYear is less than or equal to SMALLER_AMR_YEAR
        defaultMeterShouldNotBeFound("amrYear.lessThanOrEqual=" + SMALLER_AMR_YEAR);
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsLessThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear is less than DEFAULT_AMR_YEAR
        defaultMeterShouldNotBeFound("amrYear.lessThan=" + DEFAULT_AMR_YEAR);

        // Get all the meterList where amrYear is less than UPDATED_AMR_YEAR
        defaultMeterShouldBeFound("amrYear.lessThan=" + UPDATED_AMR_YEAR);
    }

    @Test
    @Transactional
    void getAllMetersByAmrYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where amrYear is greater than DEFAULT_AMR_YEAR
        defaultMeterShouldNotBeFound("amrYear.greaterThan=" + DEFAULT_AMR_YEAR);

        // Get all the meterList where amrYear is greater than SMALLER_AMR_YEAR
        defaultMeterShouldBeFound("amrYear.greaterThan=" + SMALLER_AMR_YEAR);
    }

    @Test
    @Transactional
    void getAllMetersByUtilityIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where utility equals to DEFAULT_UTILITY
        defaultMeterShouldBeFound("utility.equals=" + DEFAULT_UTILITY);

        // Get all the meterList where utility equals to UPDATED_UTILITY
        defaultMeterShouldNotBeFound("utility.equals=" + UPDATED_UTILITY);
    }

    @Test
    @Transactional
    void getAllMetersByUtilityIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where utility in DEFAULT_UTILITY or UPDATED_UTILITY
        defaultMeterShouldBeFound("utility.in=" + DEFAULT_UTILITY + "," + UPDATED_UTILITY);

        // Get all the meterList where utility equals to UPDATED_UTILITY
        defaultMeterShouldNotBeFound("utility.in=" + UPDATED_UTILITY);
    }

    @Test
    @Transactional
    void getAllMetersByUtilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where utility is not null
        defaultMeterShouldBeFound("utility.specified=true");

        // Get all the meterList where utility is null
        defaultMeterShouldNotBeFound("utility.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByLoadTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where loadType equals to DEFAULT_LOAD_TYPE
        defaultMeterShouldBeFound("loadType.equals=" + DEFAULT_LOAD_TYPE);

        // Get all the meterList where loadType equals to UPDATED_LOAD_TYPE
        defaultMeterShouldNotBeFound("loadType.equals=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllMetersByLoadTypeIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where loadType in DEFAULT_LOAD_TYPE or UPDATED_LOAD_TYPE
        defaultMeterShouldBeFound("loadType.in=" + DEFAULT_LOAD_TYPE + "," + UPDATED_LOAD_TYPE);

        // Get all the meterList where loadType equals to UPDATED_LOAD_TYPE
        defaultMeterShouldNotBeFound("loadType.in=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllMetersByLoadTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where loadType is not null
        defaultMeterShouldBeFound("loadType.specified=true");

        // Get all the meterList where loadType is null
        defaultMeterShouldNotBeFound("loadType.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price equals to DEFAULT_PRICE
        defaultMeterShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the meterList where price equals to UPDATED_PRICE
        defaultMeterShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultMeterShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the meterList where price equals to UPDATED_PRICE
        defaultMeterShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price is not null
        defaultMeterShouldBeFound("price.specified=true");

        // Get all the meterList where price is null
        defaultMeterShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price is greater than or equal to DEFAULT_PRICE
        defaultMeterShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the meterList where price is greater than or equal to UPDATED_PRICE
        defaultMeterShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price is less than or equal to DEFAULT_PRICE
        defaultMeterShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the meterList where price is less than or equal to SMALLER_PRICE
        defaultMeterShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price is less than DEFAULT_PRICE
        defaultMeterShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the meterList where price is less than UPDATED_PRICE
        defaultMeterShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMetersByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where price is greater than DEFAULT_PRICE
        defaultMeterShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the meterList where price is greater than SMALLER_PRICE
        defaultMeterShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading equals to DEFAULT_LAST_READING
        defaultMeterShouldBeFound("lastReading.equals=" + DEFAULT_LAST_READING);

        // Get all the meterList where lastReading equals to UPDATED_LAST_READING
        defaultMeterShouldNotBeFound("lastReading.equals=" + UPDATED_LAST_READING);
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading in DEFAULT_LAST_READING or UPDATED_LAST_READING
        defaultMeterShouldBeFound("lastReading.in=" + DEFAULT_LAST_READING + "," + UPDATED_LAST_READING);

        // Get all the meterList where lastReading equals to UPDATED_LAST_READING
        defaultMeterShouldNotBeFound("lastReading.in=" + UPDATED_LAST_READING);
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading is not null
        defaultMeterShouldBeFound("lastReading.specified=true");

        // Get all the meterList where lastReading is null
        defaultMeterShouldNotBeFound("lastReading.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading is greater than or equal to DEFAULT_LAST_READING
        defaultMeterShouldBeFound("lastReading.greaterThanOrEqual=" + DEFAULT_LAST_READING);

        // Get all the meterList where lastReading is greater than or equal to UPDATED_LAST_READING
        defaultMeterShouldNotBeFound("lastReading.greaterThanOrEqual=" + UPDATED_LAST_READING);
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading is less than or equal to DEFAULT_LAST_READING
        defaultMeterShouldBeFound("lastReading.lessThanOrEqual=" + DEFAULT_LAST_READING);

        // Get all the meterList where lastReading is less than or equal to SMALLER_LAST_READING
        defaultMeterShouldNotBeFound("lastReading.lessThanOrEqual=" + SMALLER_LAST_READING);
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsLessThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading is less than DEFAULT_LAST_READING
        defaultMeterShouldNotBeFound("lastReading.lessThan=" + DEFAULT_LAST_READING);

        // Get all the meterList where lastReading is less than UPDATED_LAST_READING
        defaultMeterShouldBeFound("lastReading.lessThan=" + UPDATED_LAST_READING);
    }

    @Test
    @Transactional
    void getAllMetersByLastReadingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where lastReading is greater than DEFAULT_LAST_READING
        defaultMeterShouldNotBeFound("lastReading.greaterThan=" + DEFAULT_LAST_READING);

        // Get all the meterList where lastReading is greater than SMALLER_LAST_READING
        defaultMeterShouldBeFound("lastReading.greaterThan=" + SMALLER_LAST_READING);
    }

    @Test
    @Transactional
    void getAllMetersByContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where contactEmail equals to DEFAULT_CONTACT_EMAIL
        defaultMeterShouldBeFound("contactEmail.equals=" + DEFAULT_CONTACT_EMAIL);

        // Get all the meterList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultMeterShouldNotBeFound("contactEmail.equals=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllMetersByContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where contactEmail in DEFAULT_CONTACT_EMAIL or UPDATED_CONTACT_EMAIL
        defaultMeterShouldBeFound("contactEmail.in=" + DEFAULT_CONTACT_EMAIL + "," + UPDATED_CONTACT_EMAIL);

        // Get all the meterList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultMeterShouldNotBeFound("contactEmail.in=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllMetersByContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where contactEmail is not null
        defaultMeterShouldBeFound("contactEmail.specified=true");

        // Get all the meterList where contactEmail is null
        defaultMeterShouldNotBeFound("contactEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllMetersByContactEmailContainsSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where contactEmail contains DEFAULT_CONTACT_EMAIL
        defaultMeterShouldBeFound("contactEmail.contains=" + DEFAULT_CONTACT_EMAIL);

        // Get all the meterList where contactEmail contains UPDATED_CONTACT_EMAIL
        defaultMeterShouldNotBeFound("contactEmail.contains=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllMetersByContactEmailNotContainsSomething() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList where contactEmail does not contain DEFAULT_CONTACT_EMAIL
        defaultMeterShouldNotBeFound("contactEmail.doesNotContain=" + DEFAULT_CONTACT_EMAIL);

        // Get all the meterList where contactEmail does not contain UPDATED_CONTACT_EMAIL
        defaultMeterShouldBeFound("contactEmail.doesNotContain=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllMetersByParentIsEqualToSomething() throws Exception {
        Meter parent;
        if (TestUtil.findAll(em, Meter.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            parent = MeterResourceIT.createEntity(em);
        } else {
            parent = TestUtil.findAll(em, Meter.class).get(0);
        }
        em.persist(parent);
        em.flush();
        meter.setParent(parent);
        meterRepository.saveAndFlush(meter);
        Long parentId = parent.getId();
        // Get all the meterList where parent equals to parentId
        defaultMeterShouldBeFound("parentId.equals=" + parentId);

        // Get all the meterList where parent equals to (parentId + 1)
        defaultMeterShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByAlternativeIsEqualToSomething() throws Exception {
        Meter alternative;
        if (TestUtil.findAll(em, Meter.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            alternative = MeterResourceIT.createEntity(em);
        } else {
            alternative = TestUtil.findAll(em, Meter.class).get(0);
        }
        em.persist(alternative);
        em.flush();
        meter.setAlternative(alternative);
        meterRepository.saveAndFlush(meter);
        Long alternativeId = alternative.getId();
        // Get all the meterList where alternative equals to alternativeId
        defaultMeterShouldBeFound("alternativeId.equals=" + alternativeId);

        // Get all the meterList where alternative equals to (alternativeId + 1)
        defaultMeterShouldNotBeFound("alternativeId.equals=" + (alternativeId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByPeerIsEqualToSomething() throws Exception {
        Peer peer;
        if (TestUtil.findAll(em, Peer.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            peer = PeerResourceIT.createEntity(em);
        } else {
            peer = TestUtil.findAll(em, Peer.class).get(0);
        }
        em.persist(peer);
        em.flush();
        meter.setPeer(peer);
        meterRepository.saveAndFlush(meter);
        Long peerId = peer.getId();
        // Get all the meterList where peer equals to peerId
        defaultMeterShouldBeFound("peerId.equals=" + peerId);

        // Get all the meterList where peer equals to (peerId + 1)
        defaultMeterShouldNotBeFound("peerId.equals=" + (peerId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByProviderIsEqualToSomething() throws Exception {
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            provider = ProviderResourceIT.createEntity(em);
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        em.persist(provider);
        em.flush();
        meter.setProvider(provider);
        meterRepository.saveAndFlush(meter);
        Long providerId = provider.getId();
        // Get all the meterList where provider equals to providerId
        defaultMeterShouldBeFound("providerId.equals=" + providerId);

        // Get all the meterList where provider equals to (providerId + 1)
        defaultMeterShouldNotBeFound("providerId.equals=" + (providerId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByNamespaceIsEqualToSomething() throws Exception {
        Namespace namespace;
        if (TestUtil.findAll(em, Namespace.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            namespace = NamespaceResourceIT.createEntity(em);
        } else {
            namespace = TestUtil.findAll(em, Namespace.class).get(0);
        }
        em.persist(namespace);
        em.flush();
        meter.setNamespace(namespace);
        meterRepository.saveAndFlush(meter);
        Long namespaceId = namespace.getId();
        // Get all the meterList where namespace equals to namespaceId
        defaultMeterShouldBeFound("namespaceId.equals=" + namespaceId);

        // Get all the meterList where namespace equals to (namespaceId + 1)
        defaultMeterShouldNotBeFound("namespaceId.equals=" + (namespaceId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByOwnershipsIsEqualToSomething() throws Exception {
        Ownership ownerships;
        if (TestUtil.findAll(em, Ownership.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            ownerships = OwnershipResourceIT.createEntity(em);
        } else {
            ownerships = TestUtil.findAll(em, Ownership.class).get(0);
        }
        em.persist(ownerships);
        em.flush();
        meter.addOwnerships(ownerships);
        meterRepository.saveAndFlush(meter);
        Long ownershipsId = ownerships.getId();
        // Get all the meterList where ownerships equals to ownershipsId
        defaultMeterShouldBeFound("ownershipsId.equals=" + ownershipsId);

        // Get all the meterList where ownerships equals to (ownershipsId + 1)
        defaultMeterShouldNotBeFound("ownershipsId.equals=" + (ownershipsId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByMeterIsEqualToSomething() throws Exception {
        Meter meter;
        if (TestUtil.findAll(em, Meter.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            meter = MeterResourceIT.createEntity(em);
        } else {
            meter = TestUtil.findAll(em, Meter.class).get(0);
        }
        em.persist(meter);
        em.flush();
        meter.setMeter(meter);
        meter.setParent(meter);
        meterRepository.saveAndFlush(meter);
        Long meterId = meter.getId();
        // Get all the meterList where meter equals to meterId
        defaultMeterShouldBeFound("meterId.equals=" + meterId);

        // Get all the meterList where meter equals to (meterId + 1)
        defaultMeterShouldNotBeFound("meterId.equals=" + (meterId + 1));
    }

    @Test
    @Transactional
    void getAllMetersByMeterIsEqualToSomething() throws Exception {
        Meter meter;
        if (TestUtil.findAll(em, Meter.class).isEmpty()) {
            meterRepository.saveAndFlush(meter);
            meter = MeterResourceIT.createEntity(em);
        } else {
            meter = TestUtil.findAll(em, Meter.class).get(0);
        }
        em.persist(meter);
        em.flush();
        meter.setMeter(meter);
        meter.setAlternative(meter);
        meterRepository.saveAndFlush(meter);
        Long meterId = meter.getId();
        // Get all the meterList where meter equals to meterId
        defaultMeterShouldBeFound("meterId.equals=" + meterId);

        // Get all the meterList where meter equals to (meterId + 1)
        defaultMeterShouldNotBeFound("meterId.equals=" + (meterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeterShouldBeFound(String filter) throws Exception {
        restMeterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amrWeek").value(hasItem(DEFAULT_AMR_WEEK)))
            .andExpect(jsonPath("$.[*].amrYear").value(hasItem(DEFAULT_AMR_YEAR)))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.toString())))
            .andExpect(jsonPath("$.[*].loadType").value(hasItem(DEFAULT_LOAD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].lastReading").value(hasItem(DEFAULT_LAST_READING.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)));

        // Check, that the count call also returns 1
        restMeterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeterShouldNotBeFound(String filter) throws Exception {
        restMeterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMeter() throws Exception {
        // Get the meter
        restMeterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeter() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        int databaseSizeBeforeUpdate = meterRepository.findAll().size();

        // Update the meter
        Meter updatedMeter = meterRepository.findById(meter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMeter are not directly saved in db
        em.detach(updatedMeter);
        updatedMeter
            .name(UPDATED_NAME)
            .amrWeek(UPDATED_AMR_WEEK)
            .amrYear(UPDATED_AMR_YEAR)
            .utility(UPDATED_UTILITY)
            .loadType(UPDATED_LOAD_TYPE)
            .price(UPDATED_PRICE)
            .lastReading(UPDATED_LAST_READING)
            .contactEmail(UPDATED_CONTACT_EMAIL);

        restMeterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeter))
            )
            .andExpect(status().isOk());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
        Meter testMeter = meterList.get(meterList.size() - 1);
        assertThat(testMeter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeter.getAmrWeek()).isEqualTo(UPDATED_AMR_WEEK);
        assertThat(testMeter.getAmrYear()).isEqualTo(UPDATED_AMR_YEAR);
        assertThat(testMeter.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testMeter.getLoadType()).isEqualTo(UPDATED_LOAD_TYPE);
        assertThat(testMeter.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMeter.getLastReading()).isEqualTo(UPDATED_LAST_READING);
        assertThat(testMeter.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();
        meter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();
        meter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();
        meter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeterWithPatch() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        int databaseSizeBeforeUpdate = meterRepository.findAll().size();

        // Update the meter using partial update
        Meter partialUpdatedMeter = new Meter();
        partialUpdatedMeter.setId(meter.getId());

        partialUpdatedMeter
            .name(UPDATED_NAME)
            .amrWeek(UPDATED_AMR_WEEK)
            .utility(UPDATED_UTILITY)
            .price(UPDATED_PRICE)
            .lastReading(UPDATED_LAST_READING)
            .contactEmail(UPDATED_CONTACT_EMAIL);

        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeter))
            )
            .andExpect(status().isOk());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
        Meter testMeter = meterList.get(meterList.size() - 1);
        assertThat(testMeter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeter.getAmrWeek()).isEqualTo(UPDATED_AMR_WEEK);
        assertThat(testMeter.getAmrYear()).isEqualTo(DEFAULT_AMR_YEAR);
        assertThat(testMeter.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testMeter.getLoadType()).isEqualTo(DEFAULT_LOAD_TYPE);
        assertThat(testMeter.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMeter.getLastReading()).isEqualTo(UPDATED_LAST_READING);
        assertThat(testMeter.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateMeterWithPatch() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        int databaseSizeBeforeUpdate = meterRepository.findAll().size();

        // Update the meter using partial update
        Meter partialUpdatedMeter = new Meter();
        partialUpdatedMeter.setId(meter.getId());

        partialUpdatedMeter
            .name(UPDATED_NAME)
            .amrWeek(UPDATED_AMR_WEEK)
            .amrYear(UPDATED_AMR_YEAR)
            .utility(UPDATED_UTILITY)
            .loadType(UPDATED_LOAD_TYPE)
            .price(UPDATED_PRICE)
            .lastReading(UPDATED_LAST_READING)
            .contactEmail(UPDATED_CONTACT_EMAIL);

        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeter))
            )
            .andExpect(status().isOk());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
        Meter testMeter = meterList.get(meterList.size() - 1);
        assertThat(testMeter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeter.getAmrWeek()).isEqualTo(UPDATED_AMR_WEEK);
        assertThat(testMeter.getAmrYear()).isEqualTo(UPDATED_AMR_YEAR);
        assertThat(testMeter.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testMeter.getLoadType()).isEqualTo(UPDATED_LOAD_TYPE);
        assertThat(testMeter.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMeter.getLastReading()).isEqualTo(UPDATED_LAST_READING);
        assertThat(testMeter.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();
        meter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();
        meter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();
        meter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeter() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        int databaseSizeBeforeDelete = meterRepository.findAll().size();

        // Delete the meter
        restMeterMockMvc
            .perform(delete(ENTITY_API_URL_ID, meter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
