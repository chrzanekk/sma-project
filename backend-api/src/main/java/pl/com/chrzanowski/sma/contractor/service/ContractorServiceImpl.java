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
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.service.ContactService;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
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


    public ContractorServiceImpl(ContractorDao contractorDao,
                                 ContractorMapper contractorMapper,
                                 ContactService contactService, ContactDao contactDao) {
        this.contractorDao = contractorDao;
        this.contractorMapper = contractorMapper;
        this.contactService = contactService;
        this.contactDao = contactDao;
    }

    @Override
    @Transactional
    public ContractorDTO save(ContractorDTO contractorDTO) {
        log.debug("Save contractor: {}", contractorDTO);
        validateRequiredFields(contractorDTO);

        Set<ContactBaseDTO> updatedContacts = saveNewContactIfNotExists(contractorDTO.getContacts(), contractorDTO.getCompanyId());
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

        //todo save new contact if exists in contractorDTO contactList
        Set<ContactBaseDTO> updatedContacts = saveNewContactIfNotExists(contractorDTO.getContacts(), contractorDTO.getCompanyId());

        ContractorDTO contractorWithSavedContacts = updateContractorContacts(contractorDTO, updatedContacts);

        Contractor existingContractor = contractorDao.findById(contractorWithSavedContacts.getId()).orElseThrow(()
                -> new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_FOUND, "Contractor not found"));

        contractorMapper.updateContractorFromDto(contractorWithSavedContacts, existingContractor);

        Contractor savedContractor = contractorDao.save(existingContractor);
        return contractorMapper.toDto(savedContractor);
    }

    private void setPersistedContacts(ContractorDTO contractorWithSavedContacts, Contractor existingContractor) {
        Set<Contact> persistedContacts = contractorWithSavedContacts.getContacts().stream()
                .map(contactDTO -> contactDao.findById(contactDTO.getId())
                        .orElseThrow(() -> new ContractorException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact not found: " + contactDTO.getId())))
                .collect(Collectors.toSet());
        existingContractor.setContacts(persistedContacts);
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
                .createdById(contractorDTO.getCreatedById())
                .createdByFirstName(contractorDTO.getCreatedByFirstName())
                .createdByLastName(contractorDTO.getCreatedByLastName())
                .modifiedById(contractorDTO.getModifiedById())
                .modifiedByFirstName(contractorDTO.getModifiedByFirstName())
                .modifiedByLastName(contractorDTO.getModifiedByLastName())
                .company(contractorDTO.getCompany())
                .companyId(contractorDTO.getCompanyId())
                .contacts(updatedContacts).build();
    }

    private Set<ContactBaseDTO> saveNewContactIfNotExists(Set<ContactBaseDTO> contacts, Long companyId) {
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
                            .company(contact.getCompany())
                            .createdDatetime(contact.getCreatedDatetime())
                            .lastModifiedDatetime(contact.getLastModifiedDatetime())
                            .createdById(contact.getCreatedById())
                            .createdByFirstName(contact.getCreatedByFirstName())
                            .createdByLastName(contact.getCreatedByLastName())
                            .modifiedById(contact.getModifiedById())
                            .modifiedByFirstName(contact.getModifiedByFirstName())
                            .modifiedByLastName(contact.getModifiedByLastName())
                            .companyId(companyId)
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
