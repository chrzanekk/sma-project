package pl.com.chrzanowski.sma.scaffolding.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, ScaffoldingLogPositionDTOMapper.class})
public interface ScaffoldingLogPositionAuditMapper extends EntityMapper<ScaffoldingLogPositionAuditableDTO, ScaffoldingLogPosition> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(target = "base.id", source = "id")
    @Mapping(target = "base.scaffoldingNumber", source = "scaffoldingNumber")
    @Mapping(target = "base.assemblyLocation", source = "assemblyLocation")
    @Mapping(target = "base.assemblyDate", source = "assemblyDate")
    @Mapping(target = "base.dismantlingDate", source = "dismantlingDate")
    @Mapping(target = "base.dismantlingNotificationDate", source = "dismantlingNotificationDate")
    @Mapping(target = "base.scaffoldingType", source = "scaffoldingType")
    @Mapping(target = "base.scaffoldingFullDimension", source = "scaffoldingFullDimension")
    @Mapping(target = "base.technicalProtocolStatus", source = "technicalProtocolStatus")
    @Mapping(target = "base.parentPosition", source = "parentPosition")
    @Mapping(target = "base.childPositions", source = "childPositions")
    @Mapping(target = "base.company", source = "company")
    @Mapping(target = "base.scaffoldingLog", source = "scaffoldingLog")
    @Mapping(target = "base.contractor", source = "contractor")
    @Mapping(target = "base.contractorContact", source = "contractorContact")
    @Mapping(target = "base.scaffoldingUser", source = "scaffoldingUser")
    @Mapping(target = "base.scaffoldingUserContact", source = "scaffoldingUserContact")
    ScaffoldingLogPositionAuditableDTO toDto(ScaffoldingLogPosition scaffoldingLogPosition);
}
