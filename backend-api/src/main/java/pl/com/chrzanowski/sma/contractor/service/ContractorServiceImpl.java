package pl.com.chrzanowski.sma.contractor.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorQuerySpec;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractorServiceImpl implements ContractorService {

    private final Logger log = LoggerFactory.getLogger(ContractorServiceImpl.class);
    private final ContractorDao contractorDao;
    private final ContractorMapper contractorMapper;
    private final ContractorQuerySpec contractorQuerySpec;

    public ContractorServiceImpl(ContractorDao contractorDao,
                                 ContractorMapper contractorMapper,
                                 ContractorQuerySpec contractorQuerySpec) {
        this.contractorDao = contractorDao;
        this.contractorMapper = contractorMapper;
        this.contractorQuerySpec = contractorQuerySpec;
    }

    @Override
    public ContractorDTO save(ContractorDTO contractorDTO) {
        log.debug("Save workshop: {}", contractorDTO);
        ContractorDTO contractorDTOtoSave = contractorDTO.toBuilder()
                .createdDatetime(contractorDTO.getCreatedDatetime()).build();
        Contractor contractor = contractorDao.save(contractorMapper.toEntity(contractorDTOtoSave));
        return contractorMapper.toDto(contractor);
    }

    @Override
    public ContractorDTO update(ContractorDTO contractorDTO) {
        log.debug("Update workshop: {}", contractorDTO);
        ContractorDTO contractorDTOtoUpdate = contractorDTO.toBuilder()
                .lastModifiedDatetime(contractorDTO.getLastModifiedDatetime()).build();
        Contractor contractor = contractorDao.save(contractorMapper.toEntity(contractorDTOtoUpdate));
        return contractorMapper.toDto(contractor);
    }



    @Override
    public ContractorDTO findById(Long id) {
        log.debug("Find workshop by id: {}", id);
        Optional<Contractor> workshopOptional = contractorDao.findById(id);
        return contractorMapper.toDto(workshopOptional.orElseThrow(() -> new ObjectNotFoundException("Workshop not " +
                "found")));
    }

    @Override
    public List<ContractorDTO> findAll() {
        log.debug("Find all workshops.");
        List<Contractor> contractorList = contractorDao.findAll();
        return contractorMapper.toDtoList(contractorList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete workshop by id: {}", id);
        contractorDao.deleteById(id);
    }
}
