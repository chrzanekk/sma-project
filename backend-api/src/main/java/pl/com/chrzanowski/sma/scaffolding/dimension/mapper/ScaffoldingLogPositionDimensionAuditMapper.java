package pl.com.chrzanowski.sma.scaffolding.dimension.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, CompanyDTOMapper.class})
public interface ScaffoldingLogPositionDimensionAuditMapper extends EntityMapper<ScaffoldingLogPositionDimensionAuditableDTO, ScaffoldingLogPositionDimension> {


    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "height", target = "base.height")
    @Mapping(source = "width", target = "base.width")
    @Mapping(source = "length", target = "base.length")
    @Mapping(source = "dimensionType", target = "base.dimensionType")
    @Mapping(source = "dismantlingDate", target = "base.dismantlingDate")
    @Mapping(source = "assemblyDate", target = "base.assemblyDate")
    @Mapping(source = "operationType", target = "base.operationType")
    @Mapping(source = "company", target = "base.company")
    ScaffoldingLogPositionDimensionAuditableDTO toDto(ScaffoldingLogPositionDimension scaffoldingLogPositionDimension);


}
