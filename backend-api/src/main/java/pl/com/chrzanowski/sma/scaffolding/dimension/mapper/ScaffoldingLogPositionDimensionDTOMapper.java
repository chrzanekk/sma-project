package pl.com.chrzanowski.sma.scaffolding.dimension.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.worktype.mapper.WorkTypeBaseMapper;

@Mapper(componentModel = "spring", uses = {WorkTypeBaseMapper.class, CompanyBaseMapper.class})
public interface ScaffoldingLogPositionDimensionDTOMapper extends EntityMapper<ScaffoldingLogPositionDimensionDTO, ScaffoldingLogPositionDimension> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "workType", target = "workType")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "height", target = "height")
    @Mapping(source = "width", target = "width")
    @Mapping(source = "length", target = "length")
    @Mapping(source = "dimensionType", target = "dimensionType")
    @Mapping(source = "dismantlingDate", target = "dismantlingDate")
    @Mapping(source = "assemblyDate", target = "assemblyDate")
    @Mapping(source = "scaffoldingPosition", target = "scaffoldingPosition")
    ScaffoldingLogPositionDimensionDTO toDto(ScaffoldingLogPositionDimension entity);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "workType", target = "workType")
    @Mapping(source = "scaffoldingPosition", target = "scaffoldingPosition")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    ScaffoldingLogPositionDimension toEntity(ScaffoldingLogPositionDimensionDTO dto);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "workType", ignore = true)
    @Mapping(target = "scaffoldingPosition", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(ScaffoldingLogPositionDimensionDTO dto, @MappingTarget ScaffoldingLogPositionDimension entity);

}
