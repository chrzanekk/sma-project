package pl.com.chrzanowski.sma.scaffolding.dimension.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.dimension.dao.ScaffoldingLogPositionDimensionDao;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.filter.ScaffoldingLogPositionDimensionFilter;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.filter.ScaffoldingLogPositionDimensionQuerySpec;

import java.util.List;

@Service
@Transactional
public class ScaffoldingLogPositionDimensionQueryServiceImpl implements ScaffoldingLogPositionDimensionQueryService {

    private final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionDimensionQueryServiceImpl.class);

    private final ScaffoldingLogPositionDimensionDao dao;
    private final ScaffoldingLogPositionDimensionAuditMapper auditMapper;

    public ScaffoldingLogPositionDimensionQueryServiceImpl(ScaffoldingLogPositionDimensionDao dao, ScaffoldingLogPositionDimensionAuditMapper auditMapper) {
        this.dao = dao;
        this.auditMapper = auditMapper;
    }

    @Override
    public List<ScaffoldingLogPositionDimensionAuditableDTO> findByFilter(ScaffoldingLogPositionDimensionFilter filter) {
        log.debug("Request to get Position Dimensions by filter : {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogPositionDimensionQuerySpec.buildPredicate(filter);
        return auditMapper.toDtoList(dao.findAll(specification));
    }

    @Override
    public Page<ScaffoldingLogPositionDimensionAuditableDTO> findByFilter(ScaffoldingLogPositionDimensionFilter filter, Pageable pageable) {
        log.debug("Request to get Position Dimensions by filter with page: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogPositionDimensionQuerySpec.buildPredicate(filter);
        return dao.findAll(specification, pageable).map(auditMapper::toDto);
    }
}
