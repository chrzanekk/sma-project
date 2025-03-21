package pl.com.chrzanowski.sma.company.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
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

    public CompanyJPADaoImpl(CompanyRepository companyRepository, CompanyQuerySpec companyQuerySpec) {
        this.companyRepository = companyRepository;
        this.companyQuerySpec = companyQuerySpec;
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
        JPQLQuery<Company> query = companyQuerySpec.buildQuery(specification);
        return query.fetch();
    }

    @Override
    public Page<Company> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("JPA DAO: Finding all companies by specification and page: {}, {}", specification, pageable);
        JPQLQuery<Company> query = companyQuerySpec.buildQuery(specification);
        long count = query.fetchCount();
        List<Company> content = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        return new PageImpl<>(content, pageable, count);
    }
}
