package pl.com.chrzanowski.sma.company.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.company.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao extends BaseCrudDao<Company, Long> {

    Optional<Company> findByName(String name);

    List<Company> findAll();

}
