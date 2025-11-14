package pl.com.chrzanowski.sma.scaffolding.workingtime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;


@Mapper(componentModel = "spring", uses = {CompanyBaseMapper.class, ScaffoldingLogPositionBaseMapper.class})
public interface ScaffoldingLogPositionWorkingTimeDTOMapper extends EntityMapper<ScaffoldingLogPositionWorkingTimeDTO, ScaffoldingLogPositionWorkingTime> {

    @Mapping(source = "company", target = "company")
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPositionWorkingTime toEntity(ScaffoldingLogPositionWorkingTimeDTO dto);

    @Mapping(source = "company", target = "company")
    ScaffoldingLogPositionWorkingTimeDTO toDto(ScaffoldingLogPositionWorkingTime entity);

    @Mapping(target = "company",ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromDto(ScaffoldingLogPositionWorkingTimeDTO dto, @MappingTarget ScaffoldingLogPositionWorkingTime entity);

}
