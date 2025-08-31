package pl.com.chrzanowski.sma.contracts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.contracts.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract>,
        QuerydslPredicateExecutor<Contract> {

    void deleteById(Long id);
}
