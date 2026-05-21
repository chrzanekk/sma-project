package pl.com.chrzanowski.sma.scaffolding.log.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.log.dao.ScaffoldingLogDao;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogFilter;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogQuerySpec;

import java.util.List;

@Service
@Transactional
public class ScaffoldingLogQueryServiceImpl implements ScaffoldingLogQueryService {

    private final Logger log = LoggerFactory.getLogger(ScaffoldingLogQueryServiceImpl.class);

    private final ScaffoldingLogDao scaffoldingLogDao;
    private final ScaffoldingLogAuditMapper scaffoldingLogAuditMapper;

    public ScaffoldingLogQueryServiceImpl(ScaffoldingLogDao scaffoldingLogDao, ScaffoldingLogAuditMapper scaffoldingLogAuditMapper) {
        this.scaffoldingLogDao = scaffoldingLogDao;
        this.scaffoldingLogAuditMapper = scaffoldingLogAuditMapper;
    }

    @Override
    public List<ScaffoldingLogAuditableDTO> findByFilter(ScaffoldingLogFilter filter) {
        log.debug("Request to get ScaffoldingLogs by filter: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogQuerySpec.buildPredicate(filter);
        return scaffoldingLogAuditMapper.toDtoList(scaffoldingLogDao.findAll(specification));
    }

    @Override
    public Page<ScaffoldingLogAuditableDTO> findByFilter(ScaffoldingLogFilter filter, Pageable pageable) {
        log.debug("Request to get ScaffoldingLogs by filter with page: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogQuerySpec.buildPredicate(filter);
        return scaffoldingLogDao.findAll(specification, pageable).map(scaffoldingLogAuditMapper::toDto);
    }
}
