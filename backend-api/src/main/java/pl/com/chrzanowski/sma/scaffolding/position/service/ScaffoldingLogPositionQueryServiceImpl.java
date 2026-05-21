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
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionPageDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionFilter;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionQuerySpec;

import java.util.List;

@Service
@Transactional
public class ScaffoldingLogPositionQueryServiceImpl implements ScaffoldingLogPositionQueryService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionQueryServiceImpl.class);

    private final ScaffoldingLogPositionDao scaffoldingLogPositionDao;
    private final ScaffoldingLogPositionAuditMapper scaffoldingLogPositionAuditMapper;
    private final ScaffoldingLogPositionQuerySpec querySpec;

    public ScaffoldingLogPositionQueryServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionAuditMapper scaffoldingLogPositionAuditMapper, ScaffoldingLogPositionQuerySpec querySpec) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionAuditMapper = scaffoldingLogPositionAuditMapper;
        this.querySpec = querySpec;
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

        BooleanBuilder predicate = ScaffoldingLogPositionQuerySpec.buildPredicate(filter);

        return scaffoldingLogPositionDao.findAll(predicate, pageable).map(scaffoldingLogPositionAuditMapper::toDto);
    }

    @Override
    public ScaffoldingLogPositionPageDTO findPageWithChildren(ScaffoldingLogPositionFilter filter, Pageable pageable) {
        filter.setParentPositionOnly(true);
        BooleanBuilder predicate = ScaffoldingLogPositionQuerySpec.buildPredicate(filter);
        Page<ScaffoldingLogPosition> parentPage = scaffoldingLogPositionDao.findAll(predicate, pageable);

        List<Long> parentIds = parentPage.getContent().stream()
                .map(ScaffoldingLogPosition::getId)
                .toList();

        List<ScaffoldingLogPositionAuditableDTO> childDtos = querySpec
                .fetchChildrenForParents(parentIds)
                .stream()
                .map(scaffoldingLogPositionAuditMapper::toDto)
                .toList();

        List<ScaffoldingLogPositionAuditableDTO> parentDtos = parentPage.getContent()
                .stream()
                .map(scaffoldingLogPositionAuditMapper::toDto)
                .toList();

        return ScaffoldingLogPositionPageDTO.builder()
                .parents(parentDtos)
                .children(childDtos)
                .totalElements(parentPage.getTotalElements())
                .totalPages(parentPage.getTotalPages())
                .build();
    }
}
