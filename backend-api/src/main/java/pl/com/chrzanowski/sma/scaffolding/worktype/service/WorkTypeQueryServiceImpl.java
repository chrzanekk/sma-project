package pl.com.chrzanowski.sma.scaffolding.worktype.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.worktype.dao.WorkTypeDao;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.mapper.WorkTypeAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeFilter;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeQuerySpec;

import java.util.List;

@Service
@Transactional
public class WorkTypeQueryServiceImpl implements WorkTypeQueryService {

    private final Logger log = LoggerFactory.getLogger(WorkTypeQueryServiceImpl.class);

    private final WorkTypeDao workTypeDao;
    private final WorkTypeAuditMapper workTypeAuditMapper;

    public WorkTypeQueryServiceImpl(WorkTypeDao workTypeDao, WorkTypeAuditMapper workTypeAuditMapper) {
        this.workTypeDao = workTypeDao;
        this.workTypeAuditMapper = workTypeAuditMapper;
    }

    @Override
    public List<WorkTypeAuditableDTO> findByFilter(WorkTypeFilter filter) {
        log.debug("Request to get WorkType by filter : {}", filter.toString());
        BooleanBuilder specification = WorkTypeQuerySpec.buildPredicate(filter);
        return workTypeAuditMapper.toDtoList(workTypeDao.findAll(specification));
    }

    @Override
    public Page<WorkTypeAuditableDTO> findByFilter(WorkTypeFilter filter, Pageable pageable) {
        log.debug("Request to get WorkType by filter adn page: {}", filter.toString());
        BooleanBuilder specification = WorkTypeQuerySpec.buildPredicate(filter);
        return workTypeDao.findAll(specification, pageable).map(workTypeAuditMapper::toDto);
    }
}
