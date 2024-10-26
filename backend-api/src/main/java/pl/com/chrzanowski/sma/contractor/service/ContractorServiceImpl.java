package pl.com.chrzanowski.sma.contractor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.repository.ContractorRepository;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorSpecification;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.List;
import java.util.Optional;

@Service
public class ContractorServiceImpl implements ContractorService {

    private final Logger log = LoggerFactory.getLogger(ContractorServiceImpl.class);
    private final ContractorDao contractorDao;
    private final ContractorMapper contractorMapper;
    private final ContractorSpecification contractorSpecification;

    public ContractorServiceImpl(@Qualifier("jpa") ContractorDao contractorDao,
                                 ContractorMapper contractorMapper,
                                 ContractorSpecification contractorSpecification) {
        this.contractorDao = contractorDao;
        this.contractorMapper = contractorMapper;
        this.contractorSpecification = contractorSpecification;
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
    public List<ContractorDTO> findByFilter(ContractorFilter contractorFilter) {
        log.debug("Find all workshops by filter: {}", contractorFilter);
        Specification<Contractor> spec = ContractorSpecification.createSpecification(contractorFilter);
        return contractorMapper.toDtoList(contractorDao.findAll(spec));
    }

    @Override
    public Page<ContractorDTO> findByFilterAndPage(ContractorFilter contractorFilter, Pageable pageable) {
        log.debug("Find all workshops by filter and page: {}", contractorFilter);
        Specification<Contractor> spec = ContractorSpecification.createSpecification(contractorFilter);
        return contractorDao.findAll(spec,pageable).map(contractorMapper::toDto);
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
