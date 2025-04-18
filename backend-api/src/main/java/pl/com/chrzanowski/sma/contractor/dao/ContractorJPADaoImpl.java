package pl.com.chrzanowski.sma.contractor.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.repository.ContractorRepository;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.company.model.QCompany.company;
import static pl.com.chrzanowski.sma.contact.model.QContact.contact;
import static pl.com.chrzanowski.sma.contractor.model.QContractor.contractor;

@Repository("contractorJPA")
public class ContractorJPADaoImpl implements ContractorDao {

    private final Logger log = LoggerFactory.getLogger(ContractorJPADaoImpl.class);

    private final ContractorRepository repository;
    private final ContractorQuerySpec querySpec;
    private final EntityManager em;

    public ContractorJPADaoImpl(ContractorRepository repository, ContractorQuerySpec querySpec, EntityManager em) {
        this.repository = repository;
        this.querySpec = querySpec;
        this.em = em;
    }

    @Override
    public Contractor save(Contractor contractor) {
        log.debug("DAO: Save contractor {}", contractor);
        return repository.save(contractor);
    }

    @Override
    public Optional<Contractor> findByName(String name) {
        log.debug("DAO: Find contractor with name {}", name);
        return repository.findByName(name);
    }

    @Override
    public Optional<Contractor> findById(Long id) {
        log.debug("DAO: Find contractor with id {}", id);
        return repository.findById(id);
    }

    @Override
    public Boolean existsByName(String name) {
        log.debug("DAO: Exist contractor with name {}", name);
        return repository.existsByName(name);
    }

    @Override
    public List<Contractor> findAll() {
        log.debug("DAO: Find all contractors");
        return repository.findAll();
    }

    @Override
    public Page<Contractor> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all contractors with specification for page {}, {}", specification, pageable);
        JPQLQuery<Contractor> baseQuery = querySpec.buildQuery(specification, pageable);

        long count = baseQuery.fetchCount();

        List<Contractor> contractors = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(contractors, pageable, count);
    }

    @Override
    public List<Contractor> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all contractors with specification {}", specification);
        JPQLQuery<Contractor> query = querySpec.buildQuery(specification, null);
        return query.fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete contractor with id {}", id);
        repository.deleteById(id);
    }
}
