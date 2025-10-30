package pl.com.chrzanowski.sma.employee.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.employee.repository.EmployeeRepository;
import pl.com.chrzanowski.sma.employee.service.filter.EmployeeQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.company.model.QCompany.company;
import static pl.com.chrzanowski.sma.employee.model.QEmployee.employee;

@Repository("employeeJPA")
public class EmployeeDaoImpl implements EmployeeDao {

    private final Logger log = LoggerFactory.getLogger(EmployeeDaoImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeQuerySpec employeeQuerySpec;

    public EmployeeDaoImpl(EmployeeRepository employeeRepository, EmployeeQuerySpec employeeQuerySpec) {
        this.employeeRepository = employeeRepository;
        this.employeeQuerySpec = employeeQuerySpec;
    }


    @Override
    public Employee save(Employee employee) {
        log.debug("DAO: Saving employee with id {}", employee.getId());
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        log.debug("DAO: Finding employee with id {}", id);
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> findAll() {
        log.debug("DAO: Finding all employees");
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> findAll(BooleanBuilder specification) {
        log.debug("DAO: Finding all employees by specification: {}", specification);
        return employeeQuerySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public Page<Employee> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Finding all employees by specification with page: {}", specification);
        BlazeJPAQuery<Employee> baseQuery = employeeQuerySpec.buildQuery(specification, pageable);
        baseQuery
                .leftJoin(employee.position, employee.position).fetchJoin()
                .leftJoin(employee.company, company).fetchJoin();

        PagedList<Employee> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Deleting employee with id {}", id);
        employeeRepository.deleteById(id);
    }
}
