package pl.com.chrzanowski.sma.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.employee.dto.EmployeeBaseDTO;
import pl.com.chrzanowski.sma.employee.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeBaseMapper extends EntityMapper<EmployeeBaseDTO, Employee> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Employee toEntity(EmployeeBaseDTO employeeBaseDTO);

    EmployeeBaseDTO toDto(Employee employee);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(EmployeeBaseDTO employeeBaseDTO, @MappingTarget Employee employee);

}
