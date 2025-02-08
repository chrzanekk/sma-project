package pl.com.chrzanowski.sma.contractor.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.exception.ContractorException;
import pl.com.chrzanowski.sma.common.exception.PropertyMissingException;
import pl.com.chrzanowski.sma.common.exception.error.ContractorErrorCode;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ContractorServiceImpl implements ContractorService {

    private final Logger log = LoggerFactory.getLogger(ContractorServiceImpl.class);
    private final ContractorDao contractorDao;
    private final ContractorMapper contractorMapper;
    private final UserService userService;
    private final EntityManager entityManager;

    public ContractorServiceImpl(ContractorDao contractorDao,
                                 ContractorMapper contractorMapper,
                                 UserService userService, EntityManager entityManager) {
        this.contractorDao = contractorDao;
        this.contractorMapper = contractorMapper;
        this.userService = userService;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ContractorDTO save(ContractorDTO contractorDTO) {
        log.debug("Save contractor: {}", contractorDTO);
        validateRequiredFields(contractorDTO);
        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();

        Contractor contractor = contractorMapper.toEntity(contractorDTO);
        contractor.setCreatedBy(entityManager.getReference(User.class, userInfoResponse.id()));
        contractor.setModifiedBy(entityManager.getReference(User.class, userInfoResponse.id()));
        contractor.setCreatedDatetime(Instant.now());
        contractor.setLastModifiedDatetime(Instant.now());

        Contractor savedContractor = contractorDao.save(contractor);
        return contractorMapper.toDto(savedContractor);
    }

    @Override
    @Transactional
    public ContractorDTO update(ContractorDTO contractorDTO) {
        log.debug("Update contractor: {}", contractorDTO);
        validateRequiredFields(contractorDTO);
        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();

        Contractor existingContractor = contractorDao.findById(contractorDTO.getId()).orElseThrow(()
                -> new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_FOUND, "Contractor not found"));

        Contractor updatedContractor = contractorMapper.toEntity(contractorDTO);
        updatedContractor.setId(existingContractor.getId());
        updatedContractor.setCreatedBy(existingContractor.getCreatedBy());
        updatedContractor.setCreatedDatetime(existingContractor.getCreatedDatetime());
        updatedContractor.setModifiedBy(entityManager.getReference(User.class, userInfoResponse.id()));
        updatedContractor.setLastModifiedDatetime(Instant.now());
        Contractor savedContractor = contractorDao.save(updatedContractor);
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
