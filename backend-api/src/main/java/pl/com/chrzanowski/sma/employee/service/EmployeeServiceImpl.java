package pl.com.chrzanowski.sma.employee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.common.exception.EmployeeException;
import pl.com.chrzanowski.sma.common.exception.PositionException;
import pl.com.chrzanowski.sma.common.exception.error.CompanyErrorCode;
import pl.com.chrzanowski.sma.common.exception.error.EmployeeErrorCode;
import pl.com.chrzanowski.sma.common.exception.error.PositionErrorCode;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.employee.dao.EmployeeDao;
import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
import pl.com.chrzanowski.sma.employee.mapper.EmployeeDTOMapper;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.position.dao.PositionDao;
import pl.com.chrzanowski.sma.position.model.Position;

import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeDao employeeDao;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final CompanyDao companyDao;
    private final PositionDao positionDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao, EmployeeDTOMapper employeeDTOMapper, CompanyDao companyDao, PositionDao positionDao) {
        this.employeeDao = employeeDao;
        this.employeeDTOMapper = employeeDTOMapper;
        this.companyDao = companyDao;
        this.positionDao = positionDao;
    }


    @Override
    @Transactional
    public EmployeeDTO save(EmployeeDTO createDto) {
        log.debug("Request to save Employee : {}", createDto);
        Employee employee = employeeDTOMapper.toEntity(createDto);
        employee = employeeDao.save(employee);
        return employeeDTOMapper.toDto(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO update(EmployeeDTO updateDto) {
        log.debug("Request to update Employee : {}", updateDto);

        Employee existingEmployee = employeeDao.findById(updateDto.getId()).orElseThrow(
                () -> new EmployeeException(EmployeeErrorCode.EMPLOYEE_NOT_FOUND, "Employee with id " + updateDto.getId() + " not found")
        );

        employeeDTOMapper.updateFromDto(updateDto, existingEmployee);

        if (updateDto.getCompany() != null && updateDto.getCompany().getId() != null) {
            Company company = companyDao.findById(updateDto.getCompany().getId())
                    .orElseThrow(() -> new CompanyException(
                            CompanyErrorCode.COMPANY_NOT_FOUND,
                            "Company with id " + updateDto.getCompany().getId() + " not found"
                    ));
            existingEmployee.setCompany(company);
        }
        if (updateDto.getPosition() != null && updateDto.getPosition().getId() != null) {
            Position position = positionDao.findById(updateDto.getPosition().getId())
                    .orElseThrow(() -> new PositionException(PositionErrorCode.POSITION_NOT_FOUND, "Position with id " + updateDto.getPosition().getId() + " not found"));
            existingEmployee.setPosition(position);
        }

        Employee updatedEmployee = employeeDao.save(existingEmployee);
        return employeeDTOMapper.toDto(updatedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO findById(Long aLong) {
        log.debug("Request to get Employee : {}", aLong);
        Optional<Employee> employee = employeeDao.findById(aLong);
        return employeeDTOMapper.toDto(employee.orElseThrow(() -> new EmployeeException(EmployeeErrorCode.EMPLOYEE_NOT_FOUND, "Employee with id " + aLong + " not found")));
    }

    @Override
    @Transactional
    public void delete(Long aLong) {
        log.debug("Request to delete Employee : {}", aLong);
        if (employeeDao.findById(aLong).isEmpty()) {
            throw new EmployeeException(
                    EmployeeErrorCode.EMPLOYEE_NOT_FOUND,
                    "Employee with id " + aLong + " not found"
            );
        }
        employeeDao.deleteById(aLong);
    }
}
