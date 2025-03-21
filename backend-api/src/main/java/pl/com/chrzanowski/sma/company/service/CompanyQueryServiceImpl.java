package pl.com.chrzanowski.sma.company.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;
import pl.com.chrzanowski.sma.company.service.filter.CompanyQuerySpec;

import java.util.List;

@Service
@Transactional
public class CompanyQueryServiceImpl implements CompanyQueryService {

    private final Logger log = LoggerFactory.getLogger(CompanyQueryServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyMapper companyMapper;

    public CompanyQueryServiceImpl(CompanyDao companyDao, CompanyMapper companyMapper) {
        this.companyDao = companyDao;
        this.companyMapper = companyMapper;
    }

    @Override
    public List<CompanyBaseDTO> findByFilter(CompanyFilter filter) {
        log.debug("Request to filter by filter: {}", filter);
        BooleanBuilder specification = CompanyQuerySpec.buildPredicate(filter);
        return companyMapper.toDtoList(companyDao.findAll(specification));
    }

    @Override
    public Page<CompanyBaseDTO> findByFilter(CompanyFilter filter, Pageable pageable) {
        log.debug("Request to filter by filter and page: {} {}", filter, pageable);
        BooleanBuilder specification = CompanyQuerySpec.buildPredicate(filter);
        return companyDao.findAll(specification, pageable).map(companyMapper::toDto);
    }
}
