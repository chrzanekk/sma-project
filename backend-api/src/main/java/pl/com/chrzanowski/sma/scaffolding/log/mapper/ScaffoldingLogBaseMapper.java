package pl.com.chrzanowski.sma.scaffolding.log.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;

@Mapper(componentModel = "spring")
public interface ScaffoldingLogBaseMapper extends EntityMapper<ScaffoldingLogBaseDTO, ScaffoldingLog> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLog toEntity(ScaffoldingLogBaseDTO scaffoldingLogBaseDTO);

    ScaffoldingLogBaseDTO toDto(ScaffoldingLog scaffoldingLog);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ScaffoldingLogBaseDTO dto, @MappingTarget ScaffoldingLog scaffoldingLog);
}
