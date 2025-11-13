package pl.com.chrzanowski.sma.scaffolding.position.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionFilter;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionQuerySpec;

import java.util.List;

@Service
@Transactional
public class ScaffoldingLogPositionQueryServiceImpl implements ScaffoldingLogPositionQueryService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionQueryServiceImpl.class);

    private final ScaffoldingLogPositionDao scaffoldingLogPositionDao;
    private final ScaffoldingLogPositionAuditMapper  scaffoldingLogPositionAuditMapper;

    public ScaffoldingLogPositionQueryServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionAuditMapper scaffoldingLogPositionAuditMapper) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionAuditMapper = scaffoldingLogPositionAuditMapper;
    }

    @Override
    public List<ScaffoldingLogPositionAuditableDTO> findByFilter(ScaffoldingLogPositionFilter filter) {
        log.debug("Request to get ScaffoldingLogPositions by filter: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogPositionQuerySpec.buildPredicate(filter);
        return scaffoldingLogPositionAuditMapper.toDtoList(scaffoldingLogPositionDao.findAll(specification));
    }

    @Override
    public Page<ScaffoldingLogPositionAuditableDTO> findByFilter(ScaffoldingLogPositionFilter filter, Pageable pageable) {
        log.debug("Request to get ScaffoldingLogPositions by filter with page: {}", filter.toString());
        BooleanBuilder specification = ScaffoldingLogPositionQuerySpec.buildPredicate(filter);
        return scaffoldingLogPositionDao.findAll(specification,pageable).map(scaffoldingLogPositionAuditMapper::toDto);
    }
}
