package pl.com.chrzanowski.sma.scaffolding.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeBaseMapper;
import pl.com.chrzanowski.sma.unit.mapper.UnitBaseMapper;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        UserAuditMapper.class,
        UnitBaseMapper.class,
        ScaffoldingLogBaseMapper.class,
        ContractorBaseMapper.class,
        ContactBaseMapper.class,
        CompanyBaseMapper.class,
        ScaffoldingLogPositionDimensionBaseMapper.class,
        ScaffoldingLogPositionWorkingTimeBaseMapper.class
})
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
    @Mapping(target = "base.scaffoldingFullDimensionUnit", source = "scaffoldingFullDimensionUnit")
    @Mapping(target = "base.fullWorkingTime", source = "fullWorkingTime")
    @Mapping(target = "base.technicalProtocolStatus", source = "technicalProtocolStatus")
    @Mapping(target = "base.company", source = "company")
    @Mapping(target = "base.scaffoldingLog", source = "scaffoldingLog")
    @Mapping(target = "base.contractor", source = "contractor")
    @Mapping(target = "base.contractorContact", source = "contractorContact")
    @Mapping(target = "base.scaffoldingUser", source = "scaffoldingUser")
    @Mapping(target = "base.scaffoldingUserContact", source = "scaffoldingUserContact")
    @Mapping(target = "base.dimensions", source = "dimensions")
    @Mapping(target = "base.workingTimes", source = "workingTimes")
    @Mapping(target = "base.parentPosition", ignore = true)
    @Mapping(source = "childPositions", target = "base.childPositions", qualifiedByName = "mapChildrenWithoutParent")
    ScaffoldingLogPositionAuditableDTO toDto(ScaffoldingLogPosition scaffoldingLogPosition);


    /**
     * Mapuje listę dzieci bez parentPosition (zapobiega cyklicznemu odniesieniu)
     */
    @Named("mapChildrenWithoutParent")
    default List<ScaffoldingLogPositionBaseDTO> mapChildrenWithoutParent(List<ScaffoldingLogPosition> children) {
        if (children == null) {
            return null;
        }
        return children.stream()
                .map(this::toBaseDtoWithoutParent)
                .toList();
    }

    /**
     * Mapuje pojedyncze dziecko bez parentPosition i bez childPositions
     * (płytkie mapowanie dla dzieci)
     */
    @Named("toBaseDtoWithoutParent")
    @Mapping(target = "parentPosition", ignore = true)
    @Mapping(target = "childPositions", ignore = true)
    @Mapping(target = "dimensions", source = "dimensions")
    @Mapping(target = "workingTimes", source = "workingTimes")
    ScaffoldingLogPositionBaseDTO toBaseDtoWithoutParent(ScaffoldingLogPosition entity);

    List<ScaffoldingLogPositionAuditableDTO> toDtoList(List<ScaffoldingLogPosition> entities);
}
