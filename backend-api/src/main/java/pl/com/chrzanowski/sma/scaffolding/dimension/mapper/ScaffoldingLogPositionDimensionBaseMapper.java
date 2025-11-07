package pl.com.chrzanowski.sma.scaffolding.dimension.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;

@Mapper(componentModel = "spring")
public interface ScaffoldingLogPositionDimensionBaseMapper extends EntityMapper<ScaffoldingLogPositionDimensionBaseDTO, ScaffoldingLogPositionDimension> {


    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPositionDimension toEntity(ScaffoldingLogPositionDimensionBaseDTO dto);

    ScaffoldingLogPositionDimensionBaseDTO toDto(ScaffoldingLogPositionDimension entity);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ScaffoldingLogPositionDimensionBaseDTO dto, @MappingTarget ScaffoldingLogPositionDimension entity);
}
