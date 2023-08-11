package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Meter;
import com.myapp.domain.Owner;
import com.myapp.domain.Peer;
import com.myapp.domain.enumeration.LoadType;
import com.myapp.domain.enumeration.Utility;
import com.myapp.repository.PeerRepository;
import com.myapp.service.criteria.PeerCriteria;
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
 * Integration tests for the {@link PeerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Utility DEFAULT_UTILITY = Utility.GAS;
    private static final Utility UPDATED_UTILITY = Utility.ELECTRICITY;

    private static final LoadType DEFAULT_LOAD_TYPE = LoadType.CHILL;
    private static final LoadType UPDATED_LOAD_TYPE = LoadType.CHILL_PROCESS;

    private static final String ENTITY_API_URL = "/api/peers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeerRepository peerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeerMockMvc;

    private Peer peer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peer createEntity(EntityManager em) {
        Peer peer = new Peer().name(DEFAULT_NAME).utility(DEFAULT_UTILITY).loadType(DEFAULT_LOAD_TYPE);
        return peer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peer createUpdatedEntity(EntityManager em) {
        Peer peer = new Peer().name(UPDATED_NAME).utility(UPDATED_UTILITY).loadType(UPDATED_LOAD_TYPE);
        return peer;
    }

    @BeforeEach
    public void initTest() {
        peer = createEntity(em);
    }

    @Test
    @Transactional
    void createPeer() throws Exception {
        int databaseSizeBeforeCreate = peerRepository.findAll().size();
        // Create the Peer
        restPeerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isCreated());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeCreate + 1);
        Peer testPeer = peerList.get(peerList.size() - 1);
        assertThat(testPeer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPeer.getUtility()).isEqualTo(DEFAULT_UTILITY);
        assertThat(testPeer.getLoadType()).isEqualTo(DEFAULT_LOAD_TYPE);
    }

    @Test
    @Transactional
    void createPeerWithExistingId() throws Exception {
        // Create the Peer with an existing ID
        peer.setId(1L);

        int databaseSizeBeforeCreate = peerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeers() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList
        restPeerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.toString())))
            .andExpect(jsonPath("$.[*].loadType").value(hasItem(DEFAULT_LOAD_TYPE.toString())));
    }

    @Test
    @Transactional
    void getPeer() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get the peer
        restPeerMockMvc
            .perform(get(ENTITY_API_URL_ID, peer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(peer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.utility").value(DEFAULT_UTILITY.toString()))
            .andExpect(jsonPath("$.loadType").value(DEFAULT_LOAD_TYPE.toString()));
    }

    @Test
    @Transactional
    void getPeersByIdFiltering() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        Long id = peer.getId();

        defaultPeerShouldBeFound("id.equals=" + id);
        defaultPeerShouldNotBeFound("id.notEquals=" + id);

        defaultPeerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPeerShouldNotBeFound("id.greaterThan=" + id);

        defaultPeerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPeerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where name equals to DEFAULT_NAME
        defaultPeerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the peerList where name equals to UPDATED_NAME
        defaultPeerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPeerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the peerList where name equals to UPDATED_NAME
        defaultPeerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where name is not null
        defaultPeerShouldBeFound("name.specified=true");

        // Get all the peerList where name is null
        defaultPeerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPeersByNameContainsSomething() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where name contains DEFAULT_NAME
        defaultPeerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the peerList where name contains UPDATED_NAME
        defaultPeerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where name does not contain DEFAULT_NAME
        defaultPeerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the peerList where name does not contain UPDATED_NAME
        defaultPeerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeersByUtilityIsEqualToSomething() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where utility equals to DEFAULT_UTILITY
        defaultPeerShouldBeFound("utility.equals=" + DEFAULT_UTILITY);

        // Get all the peerList where utility equals to UPDATED_UTILITY
        defaultPeerShouldNotBeFound("utility.equals=" + UPDATED_UTILITY);
    }

    @Test
    @Transactional
    void getAllPeersByUtilityIsInShouldWork() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where utility in DEFAULT_UTILITY or UPDATED_UTILITY
        defaultPeerShouldBeFound("utility.in=" + DEFAULT_UTILITY + "," + UPDATED_UTILITY);

        // Get all the peerList where utility equals to UPDATED_UTILITY
        defaultPeerShouldNotBeFound("utility.in=" + UPDATED_UTILITY);
    }

    @Test
    @Transactional
    void getAllPeersByUtilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where utility is not null
        defaultPeerShouldBeFound("utility.specified=true");

        // Get all the peerList where utility is null
        defaultPeerShouldNotBeFound("utility.specified=false");
    }

    @Test
    @Transactional
    void getAllPeersByLoadTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where loadType equals to DEFAULT_LOAD_TYPE
        defaultPeerShouldBeFound("loadType.equals=" + DEFAULT_LOAD_TYPE);

        // Get all the peerList where loadType equals to UPDATED_LOAD_TYPE
        defaultPeerShouldNotBeFound("loadType.equals=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllPeersByLoadTypeIsInShouldWork() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where loadType in DEFAULT_LOAD_TYPE or UPDATED_LOAD_TYPE
        defaultPeerShouldBeFound("loadType.in=" + DEFAULT_LOAD_TYPE + "," + UPDATED_LOAD_TYPE);

        // Get all the peerList where loadType equals to UPDATED_LOAD_TYPE
        defaultPeerShouldNotBeFound("loadType.in=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllPeersByLoadTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList where loadType is not null
        defaultPeerShouldBeFound("loadType.specified=true");

        // Get all the peerList where loadType is null
        defaultPeerShouldNotBeFound("loadType.specified=false");
    }

    @Test
    @Transactional
    void getAllPeersByMeterIsEqualToSomething() throws Exception {
        Meter meter;
        if (TestUtil.findAll(em, Meter.class).isEmpty()) {
            peerRepository.saveAndFlush(peer);
            meter = MeterResourceIT.createEntity(em);
        } else {
            meter = TestUtil.findAll(em, Meter.class).get(0);
        }
        em.persist(meter);
        em.flush();
        peer.addMeter(meter);
        peerRepository.saveAndFlush(peer);
        Long meterId = meter.getId();
        // Get all the peerList where meter equals to meterId
        defaultPeerShouldBeFound("meterId.equals=" + meterId);

        // Get all the peerList where meter equals to (meterId + 1)
        defaultPeerShouldNotBeFound("meterId.equals=" + (meterId + 1));
    }

    @Test
    @Transactional
    void getAllPeersByOwnerIsEqualToSomething() throws Exception {
        Owner owner;
        if (TestUtil.findAll(em, Owner.class).isEmpty()) {
            peerRepository.saveAndFlush(peer);
            owner = OwnerResourceIT.createEntity(em);
        } else {
            owner = TestUtil.findAll(em, Owner.class).get(0);
        }
        em.persist(owner);
        em.flush();
        peer.setOwner(owner);
        peerRepository.saveAndFlush(peer);
        Long ownerId = owner.getId();
        // Get all the peerList where owner equals to ownerId
        defaultPeerShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the peerList where owner equals to (ownerId + 1)
        defaultPeerShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeerShouldBeFound(String filter) throws Exception {
        restPeerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].utility").value(hasItem(DEFAULT_UTILITY.toString())))
            .andExpect(jsonPath("$.[*].loadType").value(hasItem(DEFAULT_LOAD_TYPE.toString())));

        // Check, that the count call also returns 1
        restPeerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeerShouldNotBeFound(String filter) throws Exception {
        restPeerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPeer() throws Exception {
        // Get the peer
        restPeerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeer() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        int databaseSizeBeforeUpdate = peerRepository.findAll().size();

        // Update the peer
        Peer updatedPeer = peerRepository.findById(peer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPeer are not directly saved in db
        em.detach(updatedPeer);
        updatedPeer.name(UPDATED_NAME).utility(UPDATED_UTILITY).loadType(UPDATED_LOAD_TYPE);

        restPeerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeer))
            )
            .andExpect(status().isOk());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
        Peer testPeer = peerList.get(peerList.size() - 1);
        assertThat(testPeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeer.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testPeer.getLoadType()).isEqualTo(UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();
        peer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();
        peer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();
        peer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeerWithPatch() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        int databaseSizeBeforeUpdate = peerRepository.findAll().size();

        // Update the peer using partial update
        Peer partialUpdatedPeer = new Peer();
        partialUpdatedPeer.setId(peer.getId());

        partialUpdatedPeer.name(UPDATED_NAME).utility(UPDATED_UTILITY);

        restPeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeer))
            )
            .andExpect(status().isOk());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
        Peer testPeer = peerList.get(peerList.size() - 1);
        assertThat(testPeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeer.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testPeer.getLoadType()).isEqualTo(DEFAULT_LOAD_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePeerWithPatch() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        int databaseSizeBeforeUpdate = peerRepository.findAll().size();

        // Update the peer using partial update
        Peer partialUpdatedPeer = new Peer();
        partialUpdatedPeer.setId(peer.getId());

        partialUpdatedPeer.name(UPDATED_NAME).utility(UPDATED_UTILITY).loadType(UPDATED_LOAD_TYPE);

        restPeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeer))
            )
            .andExpect(status().isOk());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
        Peer testPeer = peerList.get(peerList.size() - 1);
        assertThat(testPeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeer.getUtility()).isEqualTo(UPDATED_UTILITY);
        assertThat(testPeer.getLoadType()).isEqualTo(UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();
        peer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();
        peer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();
        peer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeer() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        int databaseSizeBeforeDelete = peerRepository.findAll().size();

        // Delete the peer
        restPeerMockMvc
            .perform(delete(ENTITY_API_URL_ID, peer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
