package pl.com.chrzanowski.sma.constructionsite.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.repository.ConstructionSiteRepository;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite.constructionSite;

@Repository("constructionSiteJPA")
public class ConstructionSiteJPADaoImpl implements ConstructionSiteDao {

    private final Logger log = LoggerFactory.getLogger(ConstructionSiteJPADaoImpl.class);

    private final ConstructionSiteRepository constructionSiteRepository;
    private final ConstructionSiteQuerySpec querySpec;
    private final BlazeJPAQueryFactory queryFactory;

    public ConstructionSiteJPADaoImpl(ConstructionSiteRepository constructionSiteRepository, ConstructionSiteQuerySpec querySpec, BlazeJPAQueryFactory queryFactory) {
        this.constructionSiteRepository = constructionSiteRepository;
        this.querySpec = querySpec;
        this.queryFactory = queryFactory;
    }

    @Override
    public ConstructionSite save(ConstructionSite constructionSite) {
        log.debug("DAO: Save Construction Site: {}", constructionSite.getName());
        return constructionSiteRepository.save(constructionSite);
    }

    @Override
    public Optional<ConstructionSite> findById(Long id) {
        log.debug("DAO: Find Construction Site by id: {}", id);
        return constructionSiteRepository.findById(id);
    }

    @Override
    public Page<ConstructionSite> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all Construction Sites with page: {}", specification.toString());
        BlazeJPAQuery<ConstructionSite> baseQuery = querySpec.buildQuery(specification, pageable);

        baseQuery.leftJoin(constructionSite.company).fetchJoin();

        PagedList<ConstructionSite> paged = baseQuery.fetchPage(
                (int) pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(paged, pageable, paged.getTotalSize());
    }

    @Override
    public List<ConstructionSite> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all Construction Sites: {}", specification.toString());
        return querySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete Construction Site by id: {}", id);
        constructionSiteRepository.deleteById(id);
    }
}
