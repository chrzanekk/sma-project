package pl.com.chrzanowski.sma.employee.dao;


import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.employee.model.Employee;

import java.util.List;

public interface EmployeeDao extends BaseCrudDao<Employee, Long> {

    List<Employee> findAll();

}
