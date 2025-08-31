package pl.com.chrzanowski.sma.constructionsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteBaseDTO;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;

@Mapper(componentModel = "spring")
public interface ConstructionSiteBaseMapper extends EntityMapper<ConstructionSiteBaseDTO, ConstructionSite> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ConstructionSite toEntity(ConstructionSiteBaseDTO constructionSiteBaseDTO);


    ConstructionSiteBaseDTO toDto(ConstructionSite constructionSite);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ConstructionSiteBaseDTO constructionSiteBaseDTO, @MappingTarget ConstructionSite constructionSite);
}
