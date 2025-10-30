package pl.com.chrzanowski.sma.employee.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.employee.dao.EmployeeDao;
import pl.com.chrzanowski.sma.employee.dto.EmployeeAuditableDTO;
import pl.com.chrzanowski.sma.employee.mapper.EmployeeAuditMapper;
import pl.com.chrzanowski.sma.employee.service.filter.EmployeeFilter;
import pl.com.chrzanowski.sma.employee.service.filter.EmployeeQuerySpec;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryServiceImpl.class);

    private final EmployeeDao employeeDao;
    private final EmployeeAuditMapper employeeAuditMapper;

    public EmployeeQueryServiceImpl(EmployeeDao employeeDao, EmployeeAuditMapper employeeAuditMapper) {
        this.employeeDao = employeeDao;
        this.employeeAuditMapper = employeeAuditMapper;
    }


    @Override
    public List<EmployeeAuditableDTO> findByFilter(EmployeeFilter filter) {
        log.debug("Query: Find all employees by filter : {}", filter.toString());
        BooleanBuilder builder = EmployeeQuerySpec.buildPredicate(filter);
        return employeeAuditMapper.toDtoList(employeeDao.findAll(builder));
    }

    @Override
    public Page<EmployeeAuditableDTO> findByFilter(EmployeeFilter filter, Pageable pageable) {
        log.debug("Query: Find all employees by filter and page : {}", filter.toString());
        BooleanBuilder builder = EmployeeQuerySpec.buildPredicate(filter);
        return employeeDao.findAll(builder, pageable).map(employeeAuditMapper::toDto);
    }
}
