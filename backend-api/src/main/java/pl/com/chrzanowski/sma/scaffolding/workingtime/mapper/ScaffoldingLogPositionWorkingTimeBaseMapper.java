package pl.com.chrzanowski.sma.scaffolding.workingtime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.unit.mapper.UnitBaseMapper;

@Mapper(componentModel = "spring",uses = {ScaffoldingLogPositionBaseMapper.class, UnitBaseMapper.class})
public interface ScaffoldingLogPositionWorkingTimeBaseMapper extends EntityMapper<ScaffoldingLogPositionWorkingTimeBaseDTO, ScaffoldingLogPositionWorkingTime> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPositionWorkingTime toEntity(ScaffoldingLogPositionWorkingTimeBaseDTO dto);

    ScaffoldingLogPositionWorkingTimeBaseDTO toDto(ScaffoldingLogPositionWorkingTime entity);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ScaffoldingLogPositionWorkingTimeBaseDTO dto, @MappingTarget ScaffoldingLogPositionWorkingTime entity);

}
