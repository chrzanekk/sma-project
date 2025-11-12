package pl.com.chrzanowski.sma.scaffolding.log.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogDTO;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionBaseMapper;

@Mapper(componentModel = "spring", uses = {ScaffoldingLogPositionBaseMapper.class, CompanyBaseMapper.class, ContractorBaseMapper.class, ConstructionSiteBaseMapper.class})
public interface ScaffoldingLogDTOMapper extends EntityMapper<ScaffoldingLogDTO, ScaffoldingLog> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(source = "positions", target = "positions")
    @Mapping(source = "constructionSite", target = "constructionSite")
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLog toEntity(ScaffoldingLogDTO scaffoldingLogBaseDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "additionalInfo", target = "additionalInfo")
    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(source = "positions", target = "positions")
    @Mapping(source = "constructionSite", target = "constructionSite")
    ScaffoldingLogDTO toDto(ScaffoldingLog scaffoldingLog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "contractor", ignore = true)
    @Mapping(target = "positions", ignore = true)
    @Mapping(target = "constructionSite", ignore = true)
    void updateFromDto(ScaffoldingLogDTO dto, @MappingTarget ScaffoldingLog scaffoldingLog);
}
