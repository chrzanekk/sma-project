package pl.com.chrzanowski.sma.employee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.employee.dto.EmployeeAuditableDTO;
import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
import pl.com.chrzanowski.sma.employee.service.EmployeeQueryService;
import pl.com.chrzanowski.sma.employee.service.EmployeeService;
import pl.com.chrzanowski.sma.employee.service.filter.EmployeeFilter;

@RestController
@RequestMapping(path = ApiPath.EMPLOYEE)
public class EmployeeController extends BaseCrudController<EmployeeAuditableDTO, EmployeeDTO, EmployeeDTO, EmployeeDTO, Long, EmployeeFilter> {
    public EmployeeController(EmployeeService employeeService, EmployeeQueryService employeeQueryService) {
        super(employeeService, employeeQueryService);
    }

    @Override
    protected Long extractId(EmployeeDTO employeeDTO) {
        return employeeDTO.getId();
    }
}
