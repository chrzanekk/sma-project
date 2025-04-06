package pl.com.chrzanowski.sma.contractor.service;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.exception.ContractorException;
import pl.com.chrzanowski.sma.common.exception.PropertyMissingException;
import pl.com.chrzanowski.sma.common.exception.error.ContactErrorCode;
import pl.com.chrzanowski.sma.common.exception.error.ContractorErrorCode;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.service.ContactService;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorUpdateDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractorServiceImpl implements ContractorService {

    private final Logger log = LoggerFactory.getLogger(ContractorServiceImpl.class);
    private final ContractorDao contractorDao;
    private final ContractorMapper contractorMapper;
    private final ContactService contactService;
    private final ContactDao contactDao;
    private final ContactBaseMapper contactBaseMapper;


    public ContractorServiceImpl(ContractorDao contractorDao,
                                 ContractorMapper contractorMapper,
                                 ContactService contactService, ContactDao contactDao, ContactBaseMapper contactBaseMapper) {
        this.contractorDao = contractorDao;
        this.contractorMapper = contractorMapper;
        this.contactService = contactService;
        this.contactDao = contactDao;
        this.contactBaseMapper = contactBaseMapper;
    }

    @Override
    @Transactional
    public ContractorDTO save(ContractorDTO contractorDTO) {
        log.debug("Save contractor: {}", contractorDTO);
        validateRequiredFields(contractorDTO);

        Set<ContactBaseDTO> updatedContacts = saveNewContactIfNotExists(contractorDTO.getContacts(), contractorDTO.getCompany());
        ContractorDTO contractorWithSavedContacts = updateContractorContacts(contractorDTO, updatedContacts);

        Contractor contractor = contractorMapper.toEntity(contractorWithSavedContacts);

        setPersistedContacts(contractorWithSavedContacts, contractor);

        Contractor savedContractor = contractorDao.save(contractor);
        return contractorMapper.toDto(savedContractor);
    }

    @Override
    @Transactional
    public ContractorDTO update(ContractorDTO contractorDTO) {
        log.debug("Update contractor: {}", contractorDTO);
        validateRequiredFields(contractorDTO);

        Set<ContactBaseDTO> updatedContacts = saveNewContactIfNotExists(contractorDTO.getContacts(), contractorDTO.getCompany());
        ContractorDTO contractorWithSavedContacts = updateContractorContacts(contractorDTO, updatedContacts);

        Contractor existingContractor = contractorDao.findById(contractorWithSavedContacts.getId())
                .orElseThrow(() -> new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_FOUND, "Contractor not found"));

        Set<Contact> updatedContactEntities = contractorWithSavedContacts.getContacts().stream()
                .map(dto -> contactDao.findById(dto.getId())
                        .orElseThrow(() -> new ContractorException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact not found: " + dto.getId())))
                .collect(Collectors.toSet());

        for (Contact contact : existingContractor.getContacts()) {
            contact.setContractor(null);
        }

        existingContractor.getContacts().clear();

        for (Contact contact : updatedContactEntities) {
            contact.setContractor(existingContractor);
            existingContractor.getContacts().add(contact);
        }

        contractorMapper.updateContractorFromDto(contractorWithSavedContacts, existingContractor);

        Contractor savedContractor = contractorDao.save(existingContractor);
        return contractorMapper.toDto(savedContractor);
    }


    private void setPersistedContacts(ContractorDTO contractorWithSavedContacts, Contractor contractor) {
        if (!contractor.getContacts().isEmpty()) {
            contractor.getContacts().clear();
        }
        Set<Contact> persistedContacts = contractorWithSavedContacts.getContacts().stream()
                .map(dto -> contactDao.findById(dto.getId())
                        .orElseThrow(() -> new ContractorException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact not found: " + dto.getId())))
                .collect(Collectors.toSet());

        persistedContacts.forEach(c -> c.setContractor(contractor));
        contractor.getContacts().addAll(persistedContacts);
    }

    private static ContractorDTO updateContractorContacts(ContractorDTO contractorDTO, Set<ContactBaseDTO> updatedContacts) {
        return ContractorDTO.builder()
                .id(contractorDTO.getId())
                .name(contractorDTO.getName())
                .taxNumber(contractorDTO.getTaxNumber())
                .street(contractorDTO.getStreet())
                .buildingNo(contractorDTO.getBuildingNo())
                .apartmentNo(contractorDTO.getApartmentNo())
                .postalCode(contractorDTO.getPostalCode())
                .city(contractorDTO.getCity())
                .country(contractorDTO.getCountry())
                .customer(contractorDTO.getCustomer())
                .supplier(contractorDTO.getSupplier())
                .scaffoldingUser(contractorDTO.getScaffoldingUser())
                .createdDatetime(contractorDTO.getCreatedDatetime())
                .lastModifiedDatetime(contractorDTO.getLastModifiedDatetime())
                .createdBy(contractorDTO.getCreatedBy())
                .modifiedBy(contractorDTO.getModifiedBy())
                .company(contractorDTO.getCompany())
                .contacts(updatedContacts).build();
    }

    private Set<ContactBaseDTO> saveNewContactIfNotExists(Set<ContactBaseDTO> contacts, CompanyBaseDTO company) {
        if (contacts != null) {
            List<ContactBaseDTO> filtered = contacts.stream().filter(contactBaseDTO -> contactBaseDTO.getId() != null).toList();
            List<ContactBaseDTO> result = new ArrayList<>(filtered);
            List<ContactBaseDTO> newContacts = contacts.stream()
                    .filter(contact -> contact.getId() == null)
                    .map(contact -> ContactBaseDTO.builder()
                            .id(contact.getId())
                            .firstName(contact.getFirstName())
                            .lastName(contact.getLastName())
                            .phoneNumber(contact.getPhoneNumber())
                            .email(contact.getEmail())
                            .additionalInfo(contact.getAdditionalInfo())
                            .company(company)
                            .createdDatetime(contact.getCreatedDatetime())
                            .lastModifiedDatetime(contact.getLastModifiedDatetime())
                            .createdBy(contact.getCreatedBy())
                            .modifiedBy(contact.getModifiedBy())
                            .build())
                    .collect(Collectors.toList());

            if (!newContacts.isEmpty()) {
                List<ContactBaseDTO> savedContacts = contactService.saveAllBaseContacts(newContacts);
                result.addAll(savedContacts);
            }
            return new HashSet<>(result);
        }
        return Collections.emptySet();
    }

    @Override
    public ContractorDTO updateWithChangedContacts(ContractorUpdateDTO updateDTO) {
        // wczytaj istniejącego kontrahenta
        Contractor existingContractor = contractorDao.findById(updateDTO.getId())
                .orElseThrow(() -> new ContractorException(
                        ContractorErrorCode.CONTRACTOR_NOT_FOUND,
                        "Contractor not found with id: " + updateDTO.getId()));

        // zaktualizuj dane kontrahenta (nazwa, adres, itp.) mapperem
        contractorMapper.updateContractorFromUpdateDto(updateDTO, existingContractor);

        // Dodaj nowe kontakty
        if (updateDTO.getAddedContacts() != null) {
            for (ContactBaseDTO added : updateDTO.getAddedContacts()) {
                if (added.getId() == null) {
                    // tworzymy NOWY kontakt
                    Contact newContact = contactBaseMapper.toEntity(added);
                    newContact.setContractor(existingContractor);
                    contactDao.save(newContact); // zapisujemy
                    existingContractor.getContacts().add(newContact);
                } else {
                    // załóżmy, że kontakt istnieje - trzeba go wczytać
                    Contact existing = contactDao.findById(added.getId())
                            .orElseThrow(() -> new ContractorException(
                                    ContactErrorCode.CONTACT_NOT_FOUND,
                                    "Contact not found: " + added.getId()));

                    // przypisz go do kontrahenta (o ile nie jest przypisany)
                    existing.setContractor(existingContractor);
                    existingContractor.getContacts().add(existing);
                }
            }
        }

        // Usuń kontakty
        if (updateDTO.getDeletedContacts() != null) {
            for (ContactBaseDTO deleted : updateDTO.getDeletedContacts()) {
                if (deleted.getId() != null) {
                    Contact toRemove = contactDao.findById(deleted.getId())
                            .orElseThrow(() -> new ContractorException(
                                    ContactErrorCode.CONTACT_NOT_FOUND,
                                    "Contact not found: " + deleted.getId()));
                    existingContractor.getContacts().remove(toRemove);
                    toRemove.setContractor(null);
                    contactDao.save(toRemove);
                }
            }
        }

        // zapisujemy zaktualizowanego kontrahenta
        Contractor savedContractor = contractorDao.save(existingContractor);
        return contractorMapper.toDto(savedContractor);
    }

    @Override
    public ContractorDTO findById(Long id) {
        log.debug("Find contractor by id: {}", id);
        Optional<Contractor> optionalContractor = contractorDao.findById(id);
        return contractorMapper.toDto(optionalContractor.orElseThrow(()
                -> new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_FOUND, "Contractor not found")));
    }

    @Override
    public List<ContractorDTO> findAll() {
        log.debug("Find all contractors.");
        List<Contractor> contractorList = contractorDao.findAll();
        return contractorMapper.toDtoList(contractorList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete contractor by id: {}", id);
        contractorDao.deleteById(id);
    }

    private static void validateRequiredFields(ContractorDTO contractorDTO) {
        if (StringUtils.isBlank(contractorDTO.getName())) {
            throw new PropertyMissingException(ContractorErrorCode.NAME_MISSING, "Contractor name is required", Map.of("name", contractorDTO.getName()));
        }
        if (StringUtils.isBlank(contractorDTO.getTaxNumber())) {
            throw new PropertyMissingException(ContractorErrorCode.TAX_NUMBER_MISSING, "Contractor taxNumber is required", Map.of("taxNumber", contractorDTO.getTaxNumber()));
        }
        if (StringUtils.isBlank(contractorDTO.getStreet())) {
            throw new PropertyMissingException(ContractorErrorCode.STREET_MISSING, "Contractor street is required", Map.of("street", contractorDTO.getStreet()));
        }
        if (StringUtils.isBlank(contractorDTO.getBuildingNo())) {
            throw new PropertyMissingException(ContractorErrorCode.BUILDING_NO_MISSING, "Contractor buildingNo is required", Map.of("buildingNo", contractorDTO.getBuildingNo()));
        }
        if (StringUtils.isBlank(contractorDTO.getCity())) {
            throw new PropertyMissingException(ContractorErrorCode.CITY_MISSING, "Contractor city is required", Map.of("city", contractorDTO.getCity()));
        }
        if (StringUtils.isBlank(contractorDTO.getPostalCode())) {
            throw new PropertyMissingException(ContractorErrorCode.POSTAL_CODE_MISSING, "Contractor postal code is required", Map.of("postalCode", contractorDTO.getPostalCode()));
        }
        if (contractorDTO.getCountry() == null) {
            throw new PropertyMissingException(ContractorErrorCode.COUNTRY_MISSING, "Contractor country is required");
        }
        if (contractorDTO.getCustomer() == null) {
            throw new PropertyMissingException(ContractorErrorCode.IS_CUSTOMER_MISSING, "Contractor customer is required");
        }
        if (contractorDTO.getSupplier() == null) {
            throw new PropertyMissingException(ContractorErrorCode.IS_SUPPLIER_MISSING, "Contractor supplier is required");
        }
        if (contractorDTO.getScaffoldingUser() == null) {
            throw new PropertyMissingException(ContractorErrorCode.IS_SCAFFOLDING_USER_MISSING, "Contractor scaffoldingUser is required");
        }
    }
}
