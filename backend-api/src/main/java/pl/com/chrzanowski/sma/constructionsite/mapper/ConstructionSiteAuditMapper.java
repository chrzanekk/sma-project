package pl.com.chrzanowski.sma.constructionsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, ConstructionSiteDTOMapper.class})
public interface ConstructionSiteAuditMapper extends EntityMapper<ConstructionSiteAuditableDTO, ConstructionSite> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "name", target = "base.name")
    @Mapping(source = "address", target = "base.address")
    @Mapping(source = "country", target = "base.country")
    @Mapping(source = "shortName", target = "base.shortName")
    @Mapping(source = "code", target = "base.code")
    ConstructionSiteAuditableDTO toDto(ConstructionSite constructionSite);

}
