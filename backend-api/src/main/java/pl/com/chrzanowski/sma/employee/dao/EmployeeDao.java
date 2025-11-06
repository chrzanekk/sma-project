package pl.com.chrzanowski.sma.employee.dao;


import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.employee.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    List<Employee> findAll(BooleanBuilder specification);

    Page<Employee> findAll(BooleanBuilder specification, Pageable pageable);

    void deleteById(Long id);
}
