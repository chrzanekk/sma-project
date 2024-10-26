package pl.com.chrzanowski.sma.contractor.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.List;
import java.util.Optional;

public interface ContractorDao {

    Contractor save(Contractor contractor);

    Optional<Contractor> findByName(String name);

    Optional<Contractor> findById(Long id);

    Boolean existsByName(String name);

    List<Contractor> findAll();

    Page<Contractor> findAll(Specification<Contractor> specification, Pageable pageable);

    List<Contractor> findAll(Specification<Contractor> specification);

    void deleteById(Long id);
}
