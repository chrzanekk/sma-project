package pl.com.chrzanowski.sma.contractor.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.List;
import java.util.Optional;

public interface ContractorDao extends BaseCrudDao<Contractor, Long> {

    Optional<Contractor> findByName(String name);

    Boolean existsByName(String name);

    List<Contractor> findAll();

}
