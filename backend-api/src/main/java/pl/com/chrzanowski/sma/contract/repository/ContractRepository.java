package pl.com.chrzanowski.sma.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.contract.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract>,
        QuerydslPredicateExecutor<Contract> {

    void deleteById(Long id);
}
