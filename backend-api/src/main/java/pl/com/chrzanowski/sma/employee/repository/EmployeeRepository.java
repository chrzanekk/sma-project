package pl.com.chrzanowski.sma.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import pl.com.chrzanowski.sma.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee>,
        QuerydslPredicateExecutor<Employee> {

    void deleteById(@Param("id") Long id);
}
