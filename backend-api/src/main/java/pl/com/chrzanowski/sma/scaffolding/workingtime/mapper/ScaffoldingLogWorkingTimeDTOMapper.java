package pl.com.chrzanowski.sma.scaffolding.workingtime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.scaffolding.worktype.mapper.WorkTypeBaseMapper;

@Mapper(componentModel = "spring", uses = {CompanyBaseMapper.class, WorkTypeBaseMapper.class})
public interface ScaffoldingLogWorkingTimeDTOMapper extends EntityMapper<ScaffoldingLogPositionWorkingTimeDTO, ScaffoldingLogPositionWorkingTime> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "workType", target = "workType")
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPositionWorkingTime toEntity(ScaffoldingLogPositionWorkingTimeDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "numberOfWorkers", target = "numberOfWorkers")
    @Mapping(source = "numberOfHours", target = "numberOfHours")
    @Mapping(source = "scaffoldingPositions", target = "scaffoldingPositions")
    @Mapping(source = "company", target = "company")
    @Mapping(source = "workType", target = "workType")
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPositionWorkingTimeDTO toDto(ScaffoldingLogPositionWorkingTime entity);

    @Mapping(target = "scaffoldingPositions",ignore = true)
    @Mapping(target = "company",ignore = true)
    @Mapping(target = "workType",ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromDto(ScaffoldingLogPositionWorkingTimeDTO dto, @MappingTarget ScaffoldingLogPositionWorkingTime entity);

}
