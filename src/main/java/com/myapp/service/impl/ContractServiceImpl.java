package com.myapp.service.impl;

import com.myapp.domain.Contract;
import com.myapp.repository.ContractRepository;
import com.myapp.service.ContractService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contract}.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Contract save(Contract contract) {
        log.debug("Request to save Contract : {}", contract);
        return contractRepository.save(contract);
    }

    @Override
    public Contract update(Contract contract) {
        log.debug("Request to update Contract : {}", contract);
        return contractRepository.save(contract);
    }

    @Override
    public Optional<Contract> partialUpdate(Contract contract) {
        log.debug("Request to partially update Contract : {}", contract);

        return contractRepository
            .findById(contract.getId())
            .map(existingContract -> {
                if (contract.getName() != null) {
                    existingContract.setName(contract.getName());
                }
                if (contract.getCustomerContactName() != null) {
                    existingContract.setCustomerContactName(contract.getCustomerContactName());
                }
                if (contract.getCustomerContactAddresslines() != null) {
                    existingContract.setCustomerContactAddresslines(contract.getCustomerContactAddresslines());
                }
                if (contract.getCustomerPurchaseNumber() != null) {
                    existingContract.setCustomerPurchaseNumber(contract.getCustomerPurchaseNumber());
                }
                if (contract.getKwiqlyOrderNumber() != null) {
                    existingContract.setKwiqlyOrderNumber(contract.getKwiqlyOrderNumber());
                }
                if (contract.getBasePricePerMonth() != null) {
                    existingContract.setBasePricePerMonth(contract.getBasePricePerMonth());
                }
                if (contract.getStartDate() != null) {
                    existingContract.setStartDate(contract.getStartDate());
                }
                if (contract.getEndDate() != null) {
                    existingContract.setEndDate(contract.getEndDate());
                }

                return existingContract;
            })
            .map(contractRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contract> findAll() {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contract> findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }
}
