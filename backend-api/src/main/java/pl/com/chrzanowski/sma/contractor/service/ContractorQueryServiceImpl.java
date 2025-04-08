package pl.com.chrzanowski.sma.contractor.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorAuditableDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorAuditMapper;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorQuerySpec;

import java.util.List;

@Service
@Transactional
public class ContractorQueryServiceImpl implements ContractorQueryService {

    private final Logger log = LoggerFactory.getLogger(ContractorQueryServiceImpl.class);

    private final ContractorDao contractorDao;
    private final ContractorAuditMapper contractorAuditMapper;

    public ContractorQueryServiceImpl(ContractorDao contractorDao, ContractorAuditMapper contractorAuditMapper) {
        this.contractorDao = contractorDao;
        this.contractorAuditMapper = contractorAuditMapper;
    }

    @Override
    public List<ContractorAuditableDTO> findByFilter(ContractorFilter contractorFilter) {
        log.debug("Find all contractors by filter: {}", contractorFilter);
        BooleanBuilder specification = ContractorQuerySpec.buildPredicate(contractorFilter);
        return contractorAuditMapper.toDtoList(contractorDao.findAll(specification));
    }

    @Override
    public Page<ContractorAuditableDTO> findByFilter(ContractorFilter contractorFilter, Pageable pageable) {
        log.debug("Find all contractors by filter and page: {}", contractorFilter);
        BooleanBuilder specification = ContractorQuerySpec.buildPredicate(contractorFilter);
        return contractorDao.findAll(specification, pageable).map(contractorAuditMapper::toDto);
    }
}
