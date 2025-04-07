package pl.com.chrzanowski.sma.company.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.common.exception.error.CompanyErrorCode;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyAuditableDTO;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyAuditMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;
import pl.com.chrzanowski.sma.company.service.filter.CompanyQuerySpec;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyQueryServiceImpl implements CompanyQueryService {

    private final Logger log = LoggerFactory.getLogger(CompanyQueryServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyDTOMapper companyDTOMapper;
    private final CompanyAuditMapper companyAuditMapper;

    public CompanyQueryServiceImpl(CompanyDao companyDao, CompanyDTOMapper companyDTOMapper, CompanyAuditMapper companyAuditMapper) {
        this.companyDao = companyDao;
        this.companyDTOMapper = companyDTOMapper;
        this.companyAuditMapper = companyAuditMapper;
    }

    @Override
    public List<CompanyAuditableDTO> findByFilter(CompanyFilter filter) {
        log.debug("Request to filter by filter: {}", filter);
        BooleanBuilder specification = CompanyQuerySpec.buildPredicate(filter);
        return companyAuditMapper.toDtoList(companyDao.findAll(specification));
    }

    @Override
    public Page<CompanyAuditableDTO> findByFilter(CompanyFilter filter, Pageable pageable) {
        log.debug("Request to filter by filter and page: {} {}", filter, pageable);
        BooleanBuilder specification = CompanyQuerySpec.buildPredicate(filter);
        return companyDao.findAll(specification, pageable).map(companyAuditMapper::toDto);
    }


    @Override
    public CompanyDTO findByName(String name) {
        log.debug("Request to get company by name : {}", name);
        Optional<Company> company = companyDao.findByName(name);
        return company.map(companyDTOMapper::toDto).orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with name " + name));
    }
}
