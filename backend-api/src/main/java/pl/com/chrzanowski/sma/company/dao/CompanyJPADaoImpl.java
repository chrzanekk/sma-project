package pl.com.chrzanowski.sma.company.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.company.service.filter.CompanyQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("companyJPA")
public class CompanyJPADaoImpl implements CompanyDao {

    private final Logger log = LoggerFactory.getLogger(CompanyJPADaoImpl.class);
    private final CompanyRepository companyRepository;
    private final CompanyQuerySpec companyQuerySpec;
    private final EntityManager em;

    public CompanyJPADaoImpl(CompanyRepository companyRepository, CompanyQuerySpec companyQuerySpec, EntityManager em) {
        this.companyRepository = companyRepository;
        this.companyQuerySpec = companyQuerySpec;
        this.em = em;
    }


    @Override
    public Company save(Company company) {
        log.debug("JPA DAO: Saving company: {}", company);
        return companyRepository.save(company);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("JPA DAO: Deleting company: {}", id);
        companyRepository.deleteById(id);
    }

    @Override
    public Optional<Company> findById(Long id) {
        log.debug("JPA DAO: Finding company by id: {}", id);
        return companyRepository.findById(id);
    }

    @Override
    public Optional<Company> findByName(String name) {
        log.debug("JPA DAO: Finding company by name: {}", name);
        return companyRepository.findByName(name);
    }

    @Override
    public List<Company> findAll() {
        log.debug("JPA DAO: Finding all companies");
        return companyRepository.findAll();
    }

    @Override
    public List<Company> findAll(BooleanBuilder specification) {
        log.debug("JPA DAO: Finding all companies by specification: {}", specification);
        JPQLQuery<Company> query = companyQuerySpec.buildQuery(specification, null);
        return query.fetch();
    }

    @Override
    public Page<Company> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("JPA DAO: Finding all companies by specification and page: {}, {}", specification, pageable);
        JPQLQuery<Company> baseQuery = companyQuerySpec.buildQuery(specification, pageable);

        long count = baseQuery.fetchCount();
        JPAQuery<Company> jpaQuery = getPaginationQuery(baseQuery);

        List<Company> content = jpaQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(content, pageable, count);
    }

    private JPAQuery<Company> getPaginationQuery(JPQLQuery<Company> baseQuery) {
        JPAQuery<Company> jpaQuery = (JPAQuery<Company>) baseQuery;

        EntityGraph<Company> entityGraph = em.createEntityGraph(Company.class);
        entityGraph.addSubgraph("users");

        jpaQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
        return jpaQuery;
    }
}
