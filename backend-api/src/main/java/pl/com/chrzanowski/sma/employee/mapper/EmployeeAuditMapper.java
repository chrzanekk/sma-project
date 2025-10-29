package pl.com.chrzanowski.sma.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.employee.dto.EmployeeAuditableDTO;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, EmployeeDTOMapper.class})
public interface EmployeeAuditMapper extends EntityMapper<EmployeeAuditableDTO, Employee> {
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "firstName", target = "base.firstName")
    @Mapping(source = "lastName", target = "base.lastName")
    @Mapping(source = "hourRate", target = "base.hourRate")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "position", target = "base.position")
    EmployeeAuditableDTO toDto(Employee employee);
}
