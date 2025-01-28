package pl.com.chrzanowski.sma.contractor.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Instant;
import java.util.List;
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

        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();

        Contractor existingContractor = contractorDao.findById(contractorDTO.getId()).orElseThrow(() -> new ObjectNotFoundException("Contractor not found"));

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
        return contractorMapper.toDto(optionalContractor.orElseThrow(() -> new ObjectNotFoundException("Contractor not " +
                "found")));
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
}
