package pl.com.chrzanowski.sma.position.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.position.dao.PositionDao;
import pl.com.chrzanowski.sma.position.dto.PositionAuditableDTO;
import pl.com.chrzanowski.sma.position.mapper.PositionAuditMapper;
import pl.com.chrzanowski.sma.position.service.filter.PositionFilter;
import pl.com.chrzanowski.sma.position.service.filter.PositionQuerySpec;

import java.util.List;

@Service
@Transactional
public class PositionQueryServiceImpl implements PositionQueryService {

    private final Logger log = LoggerFactory.getLogger(PositionQueryServiceImpl.class);

    private final PositionDao positionDao;
    private final PositionAuditMapper positionAuditMapper;

    public PositionQueryServiceImpl(PositionDao positionDao, PositionAuditMapper positionAuditMapper) {
        this.positionDao = positionDao;
        this.positionAuditMapper = positionAuditMapper;
    }

    @Override
    public List<PositionAuditableDTO> findByFilter(PositionFilter filter) {
        log.debug("Request to get all positions by filter : {}", filter.toString());
        BooleanBuilder specification = PositionQuerySpec.buildPredicate(filter);
        return positionAuditMapper.toDtoList(positionDao.findAll(specification));
    }

    @Override
    public Page<PositionAuditableDTO> findByFilter(PositionFilter filter, Pageable pageable) {
        log.debug("Request to get all positions by filter and page: {}", filter.toString());
        BooleanBuilder specification = PositionQuerySpec.buildPredicate(filter);
        return positionDao.findAll(specification, pageable).map(positionAuditMapper::toDto);
    }
}
