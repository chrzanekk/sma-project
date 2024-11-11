package pl.com.chrzanowski.sma.contractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.Optional;

public interface ContractorRepository extends JpaRepository<Contractor, Long>,
        JpaSpecificationExecutor<Contractor>,
        QuerydslPredicateExecutor<Contractor> {

    void deleteById(Long id);

    Optional<Contractor> findByName(String name);

    Boolean existsByName(String name);
}
