package pl.com.chrzanowski.sma.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;

@Mapper(componentModel = "spring", uses = {EmployeeBaseMapper.class, CompanyBaseMapper.class, PositionBaseMapper.class})
public interface EmployeeDTOMapper extends EntityMapper<EmployeeDTO, Employee> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "position", target = "position")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "hourRate", target = "hourRate")
    EmployeeDTO toDto(Employee employee);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "position", target = "position")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "position", target = "position")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}
