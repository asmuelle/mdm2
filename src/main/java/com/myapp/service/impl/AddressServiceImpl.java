package com.myapp.service.impl;

import com.myapp.domain.Address;
import com.myapp.repository.AddressRepository;
import com.myapp.service.AddressService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Address}.
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(Address address) {
        log.debug("Request to save Address : {}", address);
        return addressRepository.save(address);
    }

    @Override
    public Address update(Address address) {
        log.debug("Request to update Address : {}", address);
        return addressRepository.save(address);
    }

    @Override
    public Optional<Address> partialUpdate(Address address) {
        log.debug("Request to partially update Address : {}", address);

        return addressRepository
            .findById(address.getId())
            .map(existingAddress -> {
                if (address.getCity() != null) {
                    existingAddress.setCity(address.getCity());
                }
                if (address.getPostcode() != null) {
                    existingAddress.setPostcode(address.getPostcode());
                }
                if (address.getAddresslines() != null) {
                    existingAddress.setAddresslines(address.getAddresslines());
                }
                if (address.getLat() != null) {
                    existingAddress.setLat(address.getLat());
                }
                if (address.getLon() != null) {
                    existingAddress.setLon(address.getLon());
                }

                return existingAddress;
            })
            .map(addressRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAll() {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }
}
