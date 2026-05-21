package pl.com.chrzanowski.sma.scaffolding.workingtime.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dao.ScaffoldingLogPositionWorkingTimeDao;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter.ScaffoldingLogPositionWorkingTimeFilter;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter.ScaffoldingLogPositionWorkingTimeQuerySpec;

import java.util.List;

@Service
@Transactional
public class ScaffoldingLogPositionWorkingTimeQueryServiceImpl implements ScaffoldingLogPositionWorkingTimeQueryService {

    private final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionWorkingTimeQueryServiceImpl.class);

    private final ScaffoldingLogPositionWorkingTimeDao dao;
    private final ScaffoldingLogPositionWorkingTimeAuditMapper auditMapper;

    public ScaffoldingLogPositionWorkingTimeQueryServiceImpl(ScaffoldingLogPositionWorkingTimeDao dao, ScaffoldingLogPositionWorkingTimeAuditMapper auditMapper) {
        this.dao = dao;
        this.auditMapper = auditMapper;
    }

    @Override
    public List<ScaffoldingLogPositionWorkingTimeAuditableDTO> findByFilter(ScaffoldingLogPositionWorkingTimeFilter filter) {
        log.debug("Request to get Position Working time by filter: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogPositionWorkingTimeQuerySpec.buildPredicate(filter);
        return auditMapper.toDtoList(dao.findAll(specification));
    }

    @Override
    public Page<ScaffoldingLogPositionWorkingTimeAuditableDTO> findByFilter(ScaffoldingLogPositionWorkingTimeFilter filter, Pageable pageable) {
        log.debug("Request to get Position Working time by filter and page: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogPositionWorkingTimeQuerySpec.buildPredicate(filter);
        return dao.findAll(specification, pageable).map(auditMapper::toDto);
    }
}
