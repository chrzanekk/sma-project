package pl.com.chrzanowski.sma.constructionsite.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.repository.ConstructionSiteRepository;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite.constructionSite;
import static pl.com.chrzanowski.sma.contractor.model.QContractor.contractor;

@Repository("constructionSiteJPA")
public class ConstructionSiteJPADaoImpl implements ConstructionSiteDao {

    private final Logger log = LoggerFactory.getLogger(ConstructionSiteJPADaoImpl.class);

    private final ConstructionSiteRepository constructionSiteRepository;
    private final ConstructionSiteQuerySpec querySpec;
    private final JPAQueryFactory queryFactory;

    public ConstructionSiteJPADaoImpl(ConstructionSiteRepository constructionSiteRepository, ConstructionSiteQuerySpec querySpec, JPAQueryFactory queryFactory) {
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
        JPQLQuery<ConstructionSite> baseQuery = querySpec.buildQuery(specification, pageable);

        long totalElements = baseQuery.fetchCount();
        JPAQuery<ConstructionSite> jpaQuery = getPaginationQuery(baseQuery);
        List<ConstructionSite> content = jpaQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(content, pageable, totalElements);
    }

    private JPAQuery<ConstructionSite> getPaginationQuery(JPQLQuery<ConstructionSite> baseQuery) {
        JPAQuery<ConstructionSite> jpaQuery = (JPAQuery<ConstructionSite>) baseQuery;

        jpaQuery
                .leftJoin(constructionSite.company).fetchJoin()
                .leftJoin(constructionSite.siteContractors.any().contractor, contractor).fetchJoin();

        return jpaQuery;
    }

    @Override
    public List<ConstructionSite> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all Construction Sites: {}", specification.toString());
        JPQLQuery<ConstructionSite> query = querySpec.buildQuery(specification, null);
        return query.fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete Construction Site by id: {}", id);
        constructionSiteRepository.deleteById(id);
    }

    @Override
    public Page<ConstructionSite> findByContractorId(Long contractorId, Pageable pageable) {
        QConstructionSite constructionSite = QConstructionSite.constructionSite;
        List<ConstructionSite> constructionSites = queryFactory
                .selectFrom(constructionSite)
                .where(constructionSite.siteContractors.any().contractor.id.eq(contractorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(constructionSite.id.asc())
                .fetch();

        Long total = queryFactory.select(constructionSite.count())
                .from(constructionSite)
                .where(constructionSite.siteContractors.any().contractor.id.eq(contractorId))
                .fetchCount();
        return new PageImpl<>(constructionSites, pageable, total);
    }
}
