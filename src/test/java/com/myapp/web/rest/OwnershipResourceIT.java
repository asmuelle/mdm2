package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Address;
import com.myapp.domain.Meter;
import com.myapp.domain.Owner;
import com.myapp.domain.Ownership;
import com.myapp.domain.OwnershipClassification;
import com.myapp.domain.OwnershipProperty;
import com.myapp.repository.OwnershipRepository;
import com.myapp.service.OwnershipService;
import com.myapp.service.criteria.OwnershipCriteria;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OwnershipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OwnershipResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_REF = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_REF = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/ownerships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Mock
    private OwnershipRepository ownershipRepositoryMock;

    @Mock
    private OwnershipService ownershipServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnershipMockMvc;

    private Ownership ownership;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ownership createEntity(EntityManager em) {
        Ownership ownership = new Ownership()
            .name(DEFAULT_NAME)
            .clientRef(DEFAULT_CLIENT_REF)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return ownership;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ownership createUpdatedEntity(EntityManager em) {
        Ownership ownership = new Ownership()
            .name(UPDATED_NAME)
            .clientRef(UPDATED_CLIENT_REF)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return ownership;
    }

    @BeforeEach
    public void initTest() {
        ownership = createEntity(em);
    }

    @Test
    @Transactional
    void createOwnership() throws Exception {
        int databaseSizeBeforeCreate = ownershipRepository.findAll().size();
        // Create the Ownership
        restOwnershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownership)))
            .andExpect(status().isCreated());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeCreate + 1);
        Ownership testOwnership = ownershipList.get(ownershipList.size() - 1);
        assertThat(testOwnership.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOwnership.getClientRef()).isEqualTo(DEFAULT_CLIENT_REF);
        assertThat(testOwnership.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOwnership.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createOwnershipWithExistingId() throws Exception {
        // Create the Ownership with an existing ID
        ownership.setId(1L);

        int databaseSizeBeforeCreate = ownershipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownership)))
            .andExpect(status().isBadRequest());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownershipRepository.findAll().size();
        // set the field null
        ownership.setName(null);

        // Create the Ownership, which fails.

        restOwnershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownership)))
            .andExpect(status().isBadRequest());

        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOwnerships() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList
        restOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownership.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOwnershipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ownershipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOwnershipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ownershipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOwnershipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ownershipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOwnershipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ownershipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOwnership() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get the ownership
        restOwnershipMockMvc
            .perform(get(ENTITY_API_URL_ID, ownership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ownership.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.clientRef").value(DEFAULT_CLIENT_REF))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getOwnershipsByIdFiltering() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        Long id = ownership.getId();

        defaultOwnershipShouldBeFound("id.equals=" + id);
        defaultOwnershipShouldNotBeFound("id.notEquals=" + id);

        defaultOwnershipShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOwnershipShouldNotBeFound("id.greaterThan=" + id);

        defaultOwnershipShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOwnershipShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOwnershipsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where name equals to DEFAULT_NAME
        defaultOwnershipShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ownershipList where name equals to UPDATED_NAME
        defaultOwnershipShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOwnershipShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ownershipList where name equals to UPDATED_NAME
        defaultOwnershipShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where name is not null
        defaultOwnershipShouldBeFound("name.specified=true");

        // Get all the ownershipList where name is null
        defaultOwnershipShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnershipsByNameContainsSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where name contains DEFAULT_NAME
        defaultOwnershipShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ownershipList where name contains UPDATED_NAME
        defaultOwnershipShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where name does not contain DEFAULT_NAME
        defaultOwnershipShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ownershipList where name does not contain UPDATED_NAME
        defaultOwnershipShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOwnershipsByClientRefIsEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where clientRef equals to DEFAULT_CLIENT_REF
        defaultOwnershipShouldBeFound("clientRef.equals=" + DEFAULT_CLIENT_REF);

        // Get all the ownershipList where clientRef equals to UPDATED_CLIENT_REF
        defaultOwnershipShouldNotBeFound("clientRef.equals=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllOwnershipsByClientRefIsInShouldWork() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where clientRef in DEFAULT_CLIENT_REF or UPDATED_CLIENT_REF
        defaultOwnershipShouldBeFound("clientRef.in=" + DEFAULT_CLIENT_REF + "," + UPDATED_CLIENT_REF);

        // Get all the ownershipList where clientRef equals to UPDATED_CLIENT_REF
        defaultOwnershipShouldNotBeFound("clientRef.in=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllOwnershipsByClientRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where clientRef is not null
        defaultOwnershipShouldBeFound("clientRef.specified=true");

        // Get all the ownershipList where clientRef is null
        defaultOwnershipShouldNotBeFound("clientRef.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnershipsByClientRefContainsSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where clientRef contains DEFAULT_CLIENT_REF
        defaultOwnershipShouldBeFound("clientRef.contains=" + DEFAULT_CLIENT_REF);

        // Get all the ownershipList where clientRef contains UPDATED_CLIENT_REF
        defaultOwnershipShouldNotBeFound("clientRef.contains=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllOwnershipsByClientRefNotContainsSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where clientRef does not contain DEFAULT_CLIENT_REF
        defaultOwnershipShouldNotBeFound("clientRef.doesNotContain=" + DEFAULT_CLIENT_REF);

        // Get all the ownershipList where clientRef does not contain UPDATED_CLIENT_REF
        defaultOwnershipShouldBeFound("clientRef.doesNotContain=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate equals to DEFAULT_START_DATE
        defaultOwnershipShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the ownershipList where startDate equals to UPDATED_START_DATE
        defaultOwnershipShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultOwnershipShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the ownershipList where startDate equals to UPDATED_START_DATE
        defaultOwnershipShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate is not null
        defaultOwnershipShouldBeFound("startDate.specified=true");

        // Get all the ownershipList where startDate is null
        defaultOwnershipShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultOwnershipShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the ownershipList where startDate is greater than or equal to UPDATED_START_DATE
        defaultOwnershipShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate is less than or equal to DEFAULT_START_DATE
        defaultOwnershipShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the ownershipList where startDate is less than or equal to SMALLER_START_DATE
        defaultOwnershipShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate is less than DEFAULT_START_DATE
        defaultOwnershipShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the ownershipList where startDate is less than UPDATED_START_DATE
        defaultOwnershipShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where startDate is greater than DEFAULT_START_DATE
        defaultOwnershipShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the ownershipList where startDate is greater than SMALLER_START_DATE
        defaultOwnershipShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate equals to DEFAULT_END_DATE
        defaultOwnershipShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the ownershipList where endDate equals to UPDATED_END_DATE
        defaultOwnershipShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultOwnershipShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the ownershipList where endDate equals to UPDATED_END_DATE
        defaultOwnershipShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate is not null
        defaultOwnershipShouldBeFound("endDate.specified=true");

        // Get all the ownershipList where endDate is null
        defaultOwnershipShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultOwnershipShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the ownershipList where endDate is greater than or equal to UPDATED_END_DATE
        defaultOwnershipShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate is less than or equal to DEFAULT_END_DATE
        defaultOwnershipShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the ownershipList where endDate is less than or equal to SMALLER_END_DATE
        defaultOwnershipShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate is less than DEFAULT_END_DATE
        defaultOwnershipShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the ownershipList where endDate is less than UPDATED_END_DATE
        defaultOwnershipShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        // Get all the ownershipList where endDate is greater than DEFAULT_END_DATE
        defaultOwnershipShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the ownershipList where endDate is greater than SMALLER_END_DATE
        defaultOwnershipShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllOwnershipsByOwnershipPropertyIsEqualToSomething() throws Exception {
        OwnershipProperty ownershipProperty;
        if (TestUtil.findAll(em, OwnershipProperty.class).isEmpty()) {
            ownershipRepository.saveAndFlush(ownership);
            ownershipProperty = OwnershipPropertyResourceIT.createEntity(em);
        } else {
            ownershipProperty = TestUtil.findAll(em, OwnershipProperty.class).get(0);
        }
        em.persist(ownershipProperty);
        em.flush();
        ownership.addOwnershipProperty(ownershipProperty);
        ownershipRepository.saveAndFlush(ownership);
        Long ownershipPropertyId = ownershipProperty.getId();
        // Get all the ownershipList where ownershipProperty equals to ownershipPropertyId
        defaultOwnershipShouldBeFound("ownershipPropertyId.equals=" + ownershipPropertyId);

        // Get all the ownershipList where ownershipProperty equals to (ownershipPropertyId + 1)
        defaultOwnershipShouldNotBeFound("ownershipPropertyId.equals=" + (ownershipPropertyId + 1));
    }

    @Test
    @Transactional
    void getAllOwnershipsByMetersIsEqualToSomething() throws Exception {
        Meter meters;
        if (TestUtil.findAll(em, Meter.class).isEmpty()) {
            ownershipRepository.saveAndFlush(ownership);
            meters = MeterResourceIT.createEntity(em);
        } else {
            meters = TestUtil.findAll(em, Meter.class).get(0);
        }
        em.persist(meters);
        em.flush();
        ownership.addMeters(meters);
        ownershipRepository.saveAndFlush(ownership);
        Long metersId = meters.getId();
        // Get all the ownershipList where meters equals to metersId
        defaultOwnershipShouldBeFound("metersId.equals=" + metersId);

        // Get all the ownershipList where meters equals to (metersId + 1)
        defaultOwnershipShouldNotBeFound("metersId.equals=" + (metersId + 1));
    }

    @Test
    @Transactional
    void getAllOwnershipsByClassificationsIsEqualToSomething() throws Exception {
        OwnershipClassification classifications;
        if (TestUtil.findAll(em, OwnershipClassification.class).isEmpty()) {
            ownershipRepository.saveAndFlush(ownership);
            classifications = OwnershipClassificationResourceIT.createEntity(em);
        } else {
            classifications = TestUtil.findAll(em, OwnershipClassification.class).get(0);
        }
        em.persist(classifications);
        em.flush();
        ownership.addClassifications(classifications);
        ownershipRepository.saveAndFlush(ownership);
        Long classificationsId = classifications.getId();
        // Get all the ownershipList where classifications equals to classificationsId
        defaultOwnershipShouldBeFound("classificationsId.equals=" + classificationsId);

        // Get all the ownershipList where classifications equals to (classificationsId + 1)
        defaultOwnershipShouldNotBeFound("classificationsId.equals=" + (classificationsId + 1));
    }

    @Test
    @Transactional
    void getAllOwnershipsByAddressIsEqualToSomething() throws Exception {
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            ownershipRepository.saveAndFlush(ownership);
            address = AddressResourceIT.createEntity(em);
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(address);
        em.flush();
        ownership.setAddress(address);
        address.setOwnership(ownership);
        ownershipRepository.saveAndFlush(ownership);
        Long addressId = address.getId();
        // Get all the ownershipList where address equals to addressId
        defaultOwnershipShouldBeFound("addressId.equals=" + addressId);

        // Get all the ownershipList where address equals to (addressId + 1)
        defaultOwnershipShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllOwnershipsByOwnerIsEqualToSomething() throws Exception {
        Owner owner;
        if (TestUtil.findAll(em, Owner.class).isEmpty()) {
            ownershipRepository.saveAndFlush(ownership);
            owner = OwnerResourceIT.createEntity(em);
        } else {
            owner = TestUtil.findAll(em, Owner.class).get(0);
        }
        em.persist(owner);
        em.flush();
        ownership.setOwner(owner);
        ownershipRepository.saveAndFlush(ownership);
        Long ownerId = owner.getId();
        // Get all the ownershipList where owner equals to ownerId
        defaultOwnershipShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the ownershipList where owner equals to (ownerId + 1)
        defaultOwnershipShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOwnershipShouldBeFound(String filter) throws Exception {
        restOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ownership.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOwnershipShouldNotBeFound(String filter) throws Exception {
        restOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOwnership() throws Exception {
        // Get the ownership
        restOwnershipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwnership() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();

        // Update the ownership
        Ownership updatedOwnership = ownershipRepository.findById(ownership.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOwnership are not directly saved in db
        em.detach(updatedOwnership);
        updatedOwnership.name(UPDATED_NAME).clientRef(UPDATED_CLIENT_REF).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOwnership.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOwnership))
            )
            .andExpect(status().isOk());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
        Ownership testOwnership = ownershipList.get(ownershipList.size() - 1);
        assertThat(testOwnership.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwnership.getClientRef()).isEqualTo(UPDATED_CLIENT_REF);
        assertThat(testOwnership.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOwnership.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOwnership() throws Exception {
        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();
        ownership.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownership.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownership))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwnership() throws Exception {
        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();
        ownership.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownership))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwnership() throws Exception {
        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();
        ownership.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownership)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnershipWithPatch() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();

        // Update the ownership using partial update
        Ownership partialUpdatedOwnership = new Ownership();
        partialUpdatedOwnership.setId(ownership.getId());

        partialUpdatedOwnership.name(UPDATED_NAME).endDate(UPDATED_END_DATE);

        restOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwnership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwnership))
            )
            .andExpect(status().isOk());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
        Ownership testOwnership = ownershipList.get(ownershipList.size() - 1);
        assertThat(testOwnership.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwnership.getClientRef()).isEqualTo(DEFAULT_CLIENT_REF);
        assertThat(testOwnership.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOwnership.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOwnershipWithPatch() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();

        // Update the ownership using partial update
        Ownership partialUpdatedOwnership = new Ownership();
        partialUpdatedOwnership.setId(ownership.getId());

        partialUpdatedOwnership.name(UPDATED_NAME).clientRef(UPDATED_CLIENT_REF).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwnership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwnership))
            )
            .andExpect(status().isOk());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
        Ownership testOwnership = ownershipList.get(ownershipList.size() - 1);
        assertThat(testOwnership.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwnership.getClientRef()).isEqualTo(UPDATED_CLIENT_REF);
        assertThat(testOwnership.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOwnership.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOwnership() throws Exception {
        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();
        ownership.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ownership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownership))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwnership() throws Exception {
        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();
        ownership.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownership))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwnership() throws Exception {
        int databaseSizeBeforeUpdate = ownershipRepository.findAll().size();
        ownership.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ownership))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ownership in the database
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwnership() throws Exception {
        // Initialize the database
        ownershipRepository.saveAndFlush(ownership);

        int databaseSizeBeforeDelete = ownershipRepository.findAll().size();

        // Delete the ownership
        restOwnershipMockMvc
            .perform(delete(ENTITY_API_URL_ID, ownership.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ownership> ownershipList = ownershipRepository.findAll();
        assertThat(ownershipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
