package pl.com.chrzanowski.sma.employee.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.employee.dto.EmployeeAuditableDTO;
import pl.com.chrzanowski.sma.employee.service.filter.EmployeeFilter;

public interface EmployeeQueryService extends QueryService<EmployeeAuditableDTO, EmployeeFilter> {
}
