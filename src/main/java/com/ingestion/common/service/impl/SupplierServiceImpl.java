package com.ingestion.common.service.impl;

import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.common.repository.SupplierRepository;
import com.ingestion.common.service.SupplierService;
import com.ingestion.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private static final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Supplier saveSupplier(Supplier supplier) {
        log.debug("Saving supplier: {}", supplier.getName());
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findById(Long id) {
        log.debug("Finding supplier by ID: {}", id);
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findBySupplierCode(String supplierCode) {
        log.debug("Finding supplier by code: {}", supplierCode);
        return supplierRepository.findBySupplierCode(supplierCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findByEmail(String email) {
        log.debug("Finding supplier by email: {}", email);
        return supplierRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAll() {
        log.debug("Finding all suppliers");
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findByNameContaining(String name) {
        log.debug("Finding suppliers by name containing: {}", name);
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findByIsActive(Boolean isActive) {
        log.debug("Finding suppliers by active status: {}", isActive);
        return supplierRepository.findByIsActive(isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findBySupplierType(Supplier.SupplierType supplierType) {
        log.debug("Finding suppliers by type: {}", supplierType);
        return supplierRepository.findBySupplierType(supplierType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findActiveSuppliers() {
        log.debug("Finding active suppliers");
        return supplierRepository.findActiveSuppliers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findInactiveSuppliers() {
        log.debug("Finding inactive suppliers");
        return supplierRepository.findInactiveSuppliers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findActiveSuppliersByType(Supplier.SupplierType supplierType) {
        log.debug("Finding active suppliers by type: {}", supplierType);
        return supplierRepository.findActiveSuppliersByType(supplierType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findByCategory(String category) {
        log.debug("Finding suppliers by category: {}", category);
        return supplierRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findActiveByCategoryContaining(String category) {
        log.debug("Finding active suppliers by category: {}", category);
        return supplierRepository.findActiveByCategoryContaining(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllCategories() {
        log.debug("Finding all supplier categories");
        return supplierRepository.findAllCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliers(String searchTerm) {
        log.debug("Searching suppliers with term: {}", searchTerm);
        return supplierRepository.searchSuppliers(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveSuppliers() {
        log.debug("Counting active suppliers");
        return supplierRepository.countActiveSuppliers();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveSuppliersByType(Supplier.SupplierType supplierType) {
        log.debug("Counting active suppliers by type: {}", supplierType);
        return supplierRepository.countActiveSuppliersByType(supplierType);
    }

    @Override
    public Supplier createSupplier(String name, String contactPerson, String email, String phone,
                                 String addressLine1, String addressLine2, String city, String state,
                                 String postalCode, String country, Supplier.SupplierType supplierType,
                                 Set<String> categories, User createdBy) {
        log.debug("Creating supplier: {}", name);

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setContactPerson(contactPerson);
        supplier.setEmail(email);
        supplier.setPhone(phone);
        supplier.setAddressLine1(addressLine1);
        supplier.setAddressLine2(addressLine2);
        supplier.setCity(city);
        supplier.setState(state);
        supplier.setPostalCode(postalCode);
        supplier.setCountry(country);
        supplier.setSupplierType(supplierType != null ? supplierType : Supplier.SupplierType.GENERAL);
        supplier.setCategories(categories);
        supplier.setCreator(createdBy);
        supplier.setIsActive(true);

        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier createSupplier(Supplier supplier, Set<String> categories, User createdBy) {
        log.debug("Creating supplier from entity: {}", supplier.getName());

        supplier.setCategories(categories);
        supplier.setCreator(createdBy);
        supplier.setIsActive(true);

        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, String name, String contactPerson, String email, String phone,
                                 String addressLine1, String addressLine2, String city, String state,
                                 String postalCode, String country, User updatedBy) {
        log.debug("Updating supplier with ID: {}", id);

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.setName(name);
            supplier.setContactPerson(contactPerson);
            supplier.setEmail(email);
            supplier.setPhone(phone);
            supplier.setAddressLine1(addressLine1);
            supplier.setAddressLine2(addressLine2);
            supplier.setCity(city);
            supplier.setState(state);
            supplier.setPostalCode(postalCode);
            supplier.setCountry(country);
            supplier.setLastModifiedBy(updatedBy);

            return supplierRepository.save(supplier);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier updatedSupplier, User updatedBy) {
        log.debug("Updating supplier entity with ID: {}", id);

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            
            // Update fields
            supplier.setName(updatedSupplier.getName());
            supplier.setContactPerson(updatedSupplier.getContactPerson());
            supplier.setEmail(updatedSupplier.getEmail());
            supplier.setPhone(updatedSupplier.getPhone());
            supplier.setMobile(updatedSupplier.getMobile());
            supplier.setAlternatePhone(updatedSupplier.getAlternatePhone());
            supplier.setAddressLine1(updatedSupplier.getAddressLine1());
            supplier.setAddressLine2(updatedSupplier.getAddressLine2());
            supplier.setAddress(updatedSupplier.getAddress());
            supplier.setCity(updatedSupplier.getCity());
            supplier.setState(updatedSupplier.getState());
            supplier.setPostalCode(updatedSupplier.getPostalCode());
            supplier.setCountry(updatedSupplier.getCountry());
            supplier.setWebsite(updatedSupplier.getWebsite());
            supplier.setTaxId(updatedSupplier.getTaxId());
            supplier.setRegistrationNumber(updatedSupplier.getRegistrationNumber());
            supplier.setLicenseNumber(updatedSupplier.getLicenseNumber());
            supplier.setPaymentTerms(updatedSupplier.getPaymentTerms());
            supplier.setCreditLimit(updatedSupplier.getCreditLimit());
            supplier.setCreditPeriod(updatedSupplier.getCreditPeriod());
            supplier.setCreditPeriodDays(updatedSupplier.getCreditPeriodDays());
            supplier.setBankName(updatedSupplier.getBankName());
            supplier.setBankAccountNumber(updatedSupplier.getBankAccountNumber());
            supplier.setBankIfscCode(updatedSupplier.getBankIfscCode());
            supplier.setNotes(updatedSupplier.getNotes());
            supplier.setRating(updatedSupplier.getRating());
            supplier.setSupplierType(updatedSupplier.getSupplierType());
            
            supplier.setLastModifiedBy(updatedBy);

            return supplierRepository.save(supplier);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    public Supplier updateCategories(Long id, Set<String> categories) {
        log.debug("Updating categories for supplier ID: {}", id);

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.setCategories(categories);

            return supplierRepository.save(supplier);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    public Supplier updateSupplierType(Long id, Supplier.SupplierType supplierType) {
        log.debug("Updating supplier type for ID: {} to {}", id, supplierType);

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.setSupplierType(supplierType);

            return supplierRepository.save(supplier);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    public Supplier activateSupplier(Long id, User updatedBy) {
        log.debug("Activating supplier with ID: {}", id);

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.activate();
            supplier.setLastModifiedBy(updatedBy);

            return supplierRepository.save(supplier);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    public Supplier deactivateSupplier(Long id, User updatedBy) {
        log.debug("Deactivating supplier with ID: {}", id);

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.deactivate();
            supplier.setLastModifiedBy(updatedBy);

            return supplierRepository.save(supplier);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    public void deleteSupplier(Long id) {
        log.debug("Deleting supplier with ID: {}", id);
        
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Supplier not found with ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSupplierCodeUnique(String supplierCode) {
        return !supplierRepository.existsBySupplierCode(supplierCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSupplierCodeUnique(String supplierCode, Long excludeId) {
        return !supplierRepository.existsBySupplierCodeAndIdNot(supplierCode, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(String email) {
        return !supplierRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(String email, Long excludeId) {
        return !supplierRepository.existsByEmailAndIdNot(email, excludeId);
    }
}