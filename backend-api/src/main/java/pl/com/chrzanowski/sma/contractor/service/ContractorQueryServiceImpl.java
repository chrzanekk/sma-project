package pl.com.chrzanowski.sma.contractor.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorQuerySpec;

import java.util.List;

@Service
@Transactional
public class ContractorQueryServiceImpl implements ContractorQueryService {

    private final Logger log = LoggerFactory.getLogger(ContractorQueryServiceImpl.class);

    private final ContractorDao contractorDao;
    private final ContractorMapper contractorMapper;

    public ContractorQueryServiceImpl(ContractorDao contractorDao, ContractorMapper contractorMapper) {
        this.contractorDao = contractorDao;
        this.contractorMapper = contractorMapper;
    }


    @Override
    public List<ContractorDTO> findByFilter(ContractorFilter contractorFilter) {
        log.debug("Find all contractors by filter: {}", contractorFilter);
        BooleanBuilder spec = ContractorQuerySpec.buildPredicate(contractorFilter);
        return contractorMapper.toDtoList(contractorDao.findAll(spec));
    }

    @Override
    public Page<ContractorDTO> findByFilter(ContractorFilter contractorFilter, Pageable pageable) {
        log.debug("Find all contractors by filter and page: {}", contractorFilter);
        BooleanBuilder spec = ContractorQuerySpec.buildPredicate(contractorFilter);
        return contractorDao.findAll(spec, pageable).map(contractorMapper::toDto);
    }
}
