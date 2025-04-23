package pl.com.chrzanowski.sma.constructionsite.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.constructionsite.dao.ConstructionSiteDao;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteAuditMapper;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteFilter;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteQuerySpec;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConstructionSiteQueryServiceImpl implements ConstructionSiteQueryService {

    private final Logger log = LoggerFactory.getLogger(ConstructionSiteQueryServiceImpl.class);

    private final ConstructionSiteDao constructionSiteDao;
    private final ConstructionSiteAuditMapper constructionSiteAuditMapper;


    public ConstructionSiteQueryServiceImpl(ConstructionSiteDao constructionSiteDao, ConstructionSiteAuditMapper constructionSiteAuditMapper) {
        this.constructionSiteDao = constructionSiteDao;
        this.constructionSiteAuditMapper = constructionSiteAuditMapper;
    }


    @Override
    @Transactional
    public List<ConstructionSiteAuditableDTO> findByFilter(ConstructionSiteFilter filter) {
        log.debug("Query: Find all Construction Sites by filter: {}", filter.toString());
        BooleanBuilder specification = ConstructionSiteQuerySpec.buildPredicate(filter);
        return constructionSiteAuditMapper.toDtoList(constructionSiteDao.findAll(specification));
    }

    @Override
    @Transactional
    public Page<ConstructionSiteAuditableDTO> findByFilter(ConstructionSiteFilter filter, Pageable pageable) {
        log.debug("Query: Find all Construction Sites by filter and page: {}", filter.toString());
        BooleanBuilder specification = ConstructionSiteQuerySpec.buildPredicate(filter);
        return constructionSiteDao.findAll(specification, pageable).map(constructionSiteAuditMapper::toDto);
    }
}
