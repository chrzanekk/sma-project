package pl.com.chrzanowski.sma.scaffolding.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

@Mapper(componentModel = "spring")
public interface ScaffoldingLogPositionBaseMapper extends EntityMapper<ScaffoldingLogPositionBaseDTO, ScaffoldingLogPosition> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPosition toEntity(PositionBaseDTO dto);

    ScaffoldingLogPositionDTO toDto(ScaffoldingLogPosition entity);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ScaffoldingLogPositionBaseDTO dto, @MappingTarget ScaffoldingLogPosition entity);
}
