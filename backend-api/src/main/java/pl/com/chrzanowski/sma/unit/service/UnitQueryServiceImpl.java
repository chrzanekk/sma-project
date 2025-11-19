package pl.com.chrzanowski.sma.unit.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.unit.dao.UnitDao;
import pl.com.chrzanowski.sma.unit.dto.UnitAuditableDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitAuditMapper;
import pl.com.chrzanowski.sma.unit.service.filter.UnitFilter;
import pl.com.chrzanowski.sma.unit.service.filter.UnitQuerySpec;

import java.util.List;

@Service
@Transactional
public class UnitQueryServiceImpl implements UnitQueryService {

    private final Logger log = LoggerFactory.getLogger(UnitQueryServiceImpl.class);

    private final UnitDao unitDao;
    private final UnitAuditMapper auditMapper;

    public UnitQueryServiceImpl(UnitDao unitDao, UnitAuditMapper auditMapper) {
        this.unitDao = unitDao;
        this.auditMapper = auditMapper;
    }


    @Override
    public List<UnitAuditableDTO> findByFilter(UnitFilter filter) {
        log.debug("Request to get all positions by filter : {}", filter.toString());
        BooleanBuilder specification = UnitQuerySpec.buildPredicate(filter);
        return auditMapper.toDtoList(unitDao.findAll(specification));
    }

    @Override
    public Page<UnitAuditableDTO> findByFilter(UnitFilter filter, Pageable pageable) {
        log.debug("Request to get all positions by filter and page: {}", filter.toString());
        BooleanBuilder specification = UnitQuerySpec.buildPredicate(filter);
        return unitDao.findAll(specification, pageable).map(auditMapper::toDto);
    }
}
