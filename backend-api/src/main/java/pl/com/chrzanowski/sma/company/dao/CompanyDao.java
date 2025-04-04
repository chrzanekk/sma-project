package pl.com.chrzanowski.sma.company.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.company.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {

    Company save(Company company);

    void deleteById(Long id);

    Optional<Company> findById(Long id);

    Optional<Company> findByName(String name);

    List<Company> findAll();

    List<Company> findAll(BooleanBuilder specification);

    Page<Company> findAll(BooleanBuilder specification, Pageable pageable);

}
