package pl.com.chrzanowski.sma.contract.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contract.model.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractDao {

    Contract save(Contract contract);

    List<Contract> saveAll(List<Contract> contracts);

    Optional<Contract> findById(Long id);

    List<Contract> findAll();

    Page<Contract> findAll(BooleanBuilder specification, Pageable pageable);

    List<Contract> findAll(BooleanBuilder specification);

    void deleteById(Long id);

    Page<Contract> findByContractorId(Long contractorId, Pageable pageable);
}
