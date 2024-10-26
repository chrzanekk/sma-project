package pl.com.chrzanowski.sma.contractor.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.repository.ContractorRepository;

import java.util.List;
import java.util.Optional;

public class ContractorJPADaoImpl implements ContractorDao {

    private Logger log = LoggerFactory.getLogger(ContractorJPADaoImpl.class);

    private final ContractorRepository repository;

    public ContractorJPADaoImpl(ContractorRepository repository) {
        this.repository = repository;
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
    public Page<Contractor> findAll(Specification<Contractor> specification, Pageable pageable) {
        log.debug("DAO: Find all contractors with specification for page {}, {}", specification, pageable);
        return repository.findAll(specification, pageable);
    }

    @Override
    public List<Contractor> findAll(Specification<Contractor> specification) {
        log.debug("DAO: Find all contractors with specification {}", specification);
        return repository.findAll(specification);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete contractor with id {}", id);
        repository.deleteById(id);
    }
}
