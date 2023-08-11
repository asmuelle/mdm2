package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Namespace;
import com.myapp.repository.NamespaceRepository;
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
 * Integration tests for the {@link NamespaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NamespaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/namespaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NamespaceRepository namespaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNamespaceMockMvc;

    private Namespace namespace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Namespace createEntity(EntityManager em) {
        Namespace namespace = new Namespace().name(DEFAULT_NAME);
        return namespace;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Namespace createUpdatedEntity(EntityManager em) {
        Namespace namespace = new Namespace().name(UPDATED_NAME);
        return namespace;
    }

    @BeforeEach
    public void initTest() {
        namespace = createEntity(em);
    }

    @Test
    @Transactional
    void createNamespace() throws Exception {
        int databaseSizeBeforeCreate = namespaceRepository.findAll().size();
        // Create the Namespace
        restNamespaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(namespace)))
            .andExpect(status().isCreated());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeCreate + 1);
        Namespace testNamespace = namespaceList.get(namespaceList.size() - 1);
        assertThat(testNamespace.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createNamespaceWithExistingId() throws Exception {
        // Create the Namespace with an existing ID
        namespace.setId(1L);

        int databaseSizeBeforeCreate = namespaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNamespaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(namespace)))
            .andExpect(status().isBadRequest());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = namespaceRepository.findAll().size();
        // set the field null
        namespace.setName(null);

        // Create the Namespace, which fails.

        restNamespaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(namespace)))
            .andExpect(status().isBadRequest());

        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNamespaces() throws Exception {
        // Initialize the database
        namespaceRepository.saveAndFlush(namespace);

        // Get all the namespaceList
        restNamespaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(namespace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getNamespace() throws Exception {
        // Initialize the database
        namespaceRepository.saveAndFlush(namespace);

        // Get the namespace
        restNamespaceMockMvc
            .perform(get(ENTITY_API_URL_ID, namespace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(namespace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingNamespace() throws Exception {
        // Get the namespace
        restNamespaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNamespace() throws Exception {
        // Initialize the database
        namespaceRepository.saveAndFlush(namespace);

        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();

        // Update the namespace
        Namespace updatedNamespace = namespaceRepository.findById(namespace.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNamespace are not directly saved in db
        em.detach(updatedNamespace);
        updatedNamespace.name(UPDATED_NAME);

        restNamespaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNamespace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNamespace))
            )
            .andExpect(status().isOk());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
        Namespace testNamespace = namespaceList.get(namespaceList.size() - 1);
        assertThat(testNamespace.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingNamespace() throws Exception {
        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
        namespace.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNamespaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, namespace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(namespace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNamespace() throws Exception {
        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
        namespace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNamespaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(namespace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNamespace() throws Exception {
        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
        namespace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNamespaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(namespace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNamespaceWithPatch() throws Exception {
        // Initialize the database
        namespaceRepository.saveAndFlush(namespace);

        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();

        // Update the namespace using partial update
        Namespace partialUpdatedNamespace = new Namespace();
        partialUpdatedNamespace.setId(namespace.getId());

        restNamespaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNamespace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNamespace))
            )
            .andExpect(status().isOk());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
        Namespace testNamespace = namespaceList.get(namespaceList.size() - 1);
        assertThat(testNamespace.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateNamespaceWithPatch() throws Exception {
        // Initialize the database
        namespaceRepository.saveAndFlush(namespace);

        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();

        // Update the namespace using partial update
        Namespace partialUpdatedNamespace = new Namespace();
        partialUpdatedNamespace.setId(namespace.getId());

        partialUpdatedNamespace.name(UPDATED_NAME);

        restNamespaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNamespace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNamespace))
            )
            .andExpect(status().isOk());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
        Namespace testNamespace = namespaceList.get(namespaceList.size() - 1);
        assertThat(testNamespace.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingNamespace() throws Exception {
        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
        namespace.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNamespaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, namespace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(namespace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNamespace() throws Exception {
        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
        namespace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNamespaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(namespace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNamespace() throws Exception {
        int databaseSizeBeforeUpdate = namespaceRepository.findAll().size();
        namespace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNamespaceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(namespace))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Namespace in the database
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNamespace() throws Exception {
        // Initialize the database
        namespaceRepository.saveAndFlush(namespace);

        int databaseSizeBeforeDelete = namespaceRepository.findAll().size();

        // Delete the namespace
        restNamespaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, namespace.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Namespace> namespaceList = namespaceRepository.findAll();
        assertThat(namespaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
