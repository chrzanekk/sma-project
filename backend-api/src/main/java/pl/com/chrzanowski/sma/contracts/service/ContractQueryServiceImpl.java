package pl.com.chrzanowski.sma.contracts.service;


import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.contracts.dao.ContractDao;
import pl.com.chrzanowski.sma.contracts.dto.ContractAuditableDTO;
import pl.com.chrzanowski.sma.contracts.mapper.ContractAuditMapper;
import pl.com.chrzanowski.sma.contracts.service.filter.ContractFilter;
import pl.com.chrzanowski.sma.contracts.service.filter.ContractQuerySpec;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContractQueryServiceImpl implements ContractQueryService {

    private final Logger log = LoggerFactory.getLogger(ContractQueryServiceImpl.class);

    private final ContractDao contractDao;
    private final ContractAuditMapper contractAuditMapper;


    public ContractQueryServiceImpl(ContractDao contractDao, ContractAuditMapper contractAuditMapper) {
        this.contractDao = contractDao;
        this.contractAuditMapper = contractAuditMapper;
    }

    @Override
    public List<ContractAuditableDTO> findByFilter(ContractFilter filter) {
        log.debug("Query: Find all contracts by filter : {}", filter.toString());
        BooleanBuilder specification = ContractQuerySpec.buildPredicate(filter);
        return contractAuditMapper.toDtoList(contractDao.findAll(specification));
    }

    @Override
    public Page<ContractAuditableDTO> findByFilter(ContractFilter filter, Pageable pageable) {
        log.debug("Query: Find all contracts by filter and page : {}", filter.toString());
        BooleanBuilder specification = ContractQuerySpec.buildPredicate(filter);
        return contractDao.findAll(specification, pageable).map(contractAuditMapper::toDto);
    }
}
