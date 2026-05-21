package pl.com.chrzanowski.sma.contract.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.contract.model.Contract;

import java.util.List;

public interface ContractDao extends BaseCrudDao<Contract, Long> {

    List<Contract> saveAll(List<Contract> contracts);

    List<Contract> findAll();

    Page<Contract> findByContractorId(Long contractorId, Pageable pageable);
}
