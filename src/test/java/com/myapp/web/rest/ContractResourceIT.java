package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Contract;
import com.myapp.repository.ContractRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CONTACT_ADDRESSLINES = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CONTACT_ADDRESSLINES = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_PURCHASE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_PURCHASE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_KWIQLY_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_KWIQLY_ORDER_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_BASE_PRICE_PER_MONTH = 1;
    private static final Integer UPDATED_BASE_PRICE_PER_MONTH = 2;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .name(DEFAULT_NAME)
            .customerContactName(DEFAULT_CUSTOMER_CONTACT_NAME)
            .customerContactAddresslines(DEFAULT_CUSTOMER_CONTACT_ADDRESSLINES)
            .customerPurchaseNumber(DEFAULT_CUSTOMER_PURCHASE_NUMBER)
            .kwiqlyOrderNumber(DEFAULT_KWIQLY_ORDER_NUMBER)
            .basePricePerMonth(DEFAULT_BASE_PRICE_PER_MONTH)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .name(UPDATED_NAME)
            .customerContactName(UPDATED_CUSTOMER_CONTACT_NAME)
            .customerContactAddresslines(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES)
            .customerPurchaseNumber(UPDATED_CUSTOMER_PURCHASE_NUMBER)
            .kwiqlyOrderNumber(UPDATED_KWIQLY_ORDER_NUMBER)
            .basePricePerMonth(UPDATED_BASE_PRICE_PER_MONTH)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();
        // Create the Contract
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContract.getCustomerContactName()).isEqualTo(DEFAULT_CUSTOMER_CONTACT_NAME);
        assertThat(testContract.getCustomerContactAddresslines()).isEqualTo(DEFAULT_CUSTOMER_CONTACT_ADDRESSLINES);
        assertThat(testContract.getCustomerPurchaseNumber()).isEqualTo(DEFAULT_CUSTOMER_PURCHASE_NUMBER);
        assertThat(testContract.getKwiqlyOrderNumber()).isEqualTo(DEFAULT_KWIQLY_ORDER_NUMBER);
        assertThat(testContract.getBasePricePerMonth()).isEqualTo(DEFAULT_BASE_PRICE_PER_MONTH);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);

        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].customerContactName").value(hasItem(DEFAULT_CUSTOMER_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].customerContactAddresslines").value(hasItem(DEFAULT_CUSTOMER_CONTACT_ADDRESSLINES)))
            .andExpect(jsonPath("$.[*].customerPurchaseNumber").value(hasItem(DEFAULT_CUSTOMER_PURCHASE_NUMBER)))
            .andExpect(jsonPath("$.[*].kwiqlyOrderNumber").value(hasItem(DEFAULT_KWIQLY_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].basePricePerMonth").value(hasItem(DEFAULT_BASE_PRICE_PER_MONTH)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.customerContactName").value(DEFAULT_CUSTOMER_CONTACT_NAME))
            .andExpect(jsonPath("$.customerContactAddresslines").value(DEFAULT_CUSTOMER_CONTACT_ADDRESSLINES))
            .andExpect(jsonPath("$.customerPurchaseNumber").value(DEFAULT_CUSTOMER_PURCHASE_NUMBER))
            .andExpect(jsonPath("$.kwiqlyOrderNumber").value(DEFAULT_KWIQLY_ORDER_NUMBER))
            .andExpect(jsonPath("$.basePricePerMonth").value(DEFAULT_BASE_PRICE_PER_MONTH))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .name(UPDATED_NAME)
            .customerContactName(UPDATED_CUSTOMER_CONTACT_NAME)
            .customerContactAddresslines(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES)
            .customerPurchaseNumber(UPDATED_CUSTOMER_PURCHASE_NUMBER)
            .kwiqlyOrderNumber(UPDATED_KWIQLY_ORDER_NUMBER)
            .basePricePerMonth(UPDATED_BASE_PRICE_PER_MONTH)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContract.getCustomerContactName()).isEqualTo(UPDATED_CUSTOMER_CONTACT_NAME);
        assertThat(testContract.getCustomerContactAddresslines()).isEqualTo(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES);
        assertThat(testContract.getCustomerPurchaseNumber()).isEqualTo(UPDATED_CUSTOMER_PURCHASE_NUMBER);
        assertThat(testContract.getKwiqlyOrderNumber()).isEqualTo(UPDATED_KWIQLY_ORDER_NUMBER);
        assertThat(testContract.getBasePricePerMonth()).isEqualTo(UPDATED_BASE_PRICE_PER_MONTH);
        assertThat(testContract.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .customerContactAddresslines(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES)
            .customerPurchaseNumber(UPDATED_CUSTOMER_PURCHASE_NUMBER)
            .endDate(UPDATED_END_DATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContract.getCustomerContactName()).isEqualTo(DEFAULT_CUSTOMER_CONTACT_NAME);
        assertThat(testContract.getCustomerContactAddresslines()).isEqualTo(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES);
        assertThat(testContract.getCustomerPurchaseNumber()).isEqualTo(UPDATED_CUSTOMER_PURCHASE_NUMBER);
        assertThat(testContract.getKwiqlyOrderNumber()).isEqualTo(DEFAULT_KWIQLY_ORDER_NUMBER);
        assertThat(testContract.getBasePricePerMonth()).isEqualTo(DEFAULT_BASE_PRICE_PER_MONTH);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .name(UPDATED_NAME)
            .customerContactName(UPDATED_CUSTOMER_CONTACT_NAME)
            .customerContactAddresslines(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES)
            .customerPurchaseNumber(UPDATED_CUSTOMER_PURCHASE_NUMBER)
            .kwiqlyOrderNumber(UPDATED_KWIQLY_ORDER_NUMBER)
            .basePricePerMonth(UPDATED_BASE_PRICE_PER_MONTH)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContract.getCustomerContactName()).isEqualTo(UPDATED_CUSTOMER_CONTACT_NAME);
        assertThat(testContract.getCustomerContactAddresslines()).isEqualTo(UPDATED_CUSTOMER_CONTACT_ADDRESSLINES);
        assertThat(testContract.getCustomerPurchaseNumber()).isEqualTo(UPDATED_CUSTOMER_PURCHASE_NUMBER);
        assertThat(testContract.getKwiqlyOrderNumber()).isEqualTo(UPDATED_KWIQLY_ORDER_NUMBER);
        assertThat(testContract.getBasePricePerMonth()).isEqualTo(UPDATED_BASE_PRICE_PER_MONTH);
        assertThat(testContract.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
