package pl.com.chrzanowski.sma.scaffolding.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

@Mapper(componentModel = "spring", uses = {ScaffoldingLogPositionBaseMapper.class, CompanyBaseMapper.class, ContractorBaseMapper.class, ContactBaseMapper.class, ScaffoldingLogBaseMapper.class})
public interface ScaffoldingLogPositionDTOMapper extends EntityMapper<ScaffoldingLogPositionBaseDTO, ScaffoldingLogPosition> {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "scaffoldingNumber", source = "scaffoldingNumber")
    @Mapping(target = "assemblyLocation", source = "assemblyLocation")
    @Mapping(target = "assemblyDate", source = "assemblyDate")
    @Mapping(target = "dismantlingDate", source = "dismantlingDate")
    @Mapping(target = "dismantlingNotificationDate", source = "dismantlingNotificationDate")
    @Mapping(target = "scaffoldingType", source = "scaffoldingType")
    @Mapping(target = "scaffoldingFullDimension", source = "scaffoldingFullDimension")
    @Mapping(target = "technicalProtocolStatus", source = "technicalProtocolStatus")
    @Mapping(target = "parentPosition", source = "parentPosition")
    @Mapping(target = "childPositions", source = "childPositions")
    @Mapping(target = "company", source = "company")
    @Mapping(target = "scaffoldingLog", source = "scaffoldingLog")
    @Mapping(target = "contractor", source = "contractor")
    @Mapping(target = "contractorContact", source = "contractorContact")
    @Mapping(target = "scaffoldingUser", source = "scaffoldingUser")
    @Mapping(target = "scaffoldingUserContact", source = "scaffoldingUserContact")
    ScaffoldingLogPositionDTO toDto(ScaffoldingLogPosition entity);

    @Mapping(target = "company", source = "company")
    @Mapping(target = "scaffoldingLog", source = "scaffoldingLog")
    @Mapping(target = "contractor", source = "contractor")
    @Mapping(target = "contractorContact", source = "contractorContact")
    @Mapping(target = "scaffoldingUser", source = "scaffoldingUser")
    @Mapping(target = "scaffoldingUserContact", source = "scaffoldingUserContact")
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPosition toEntity(PositionBaseDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "scaffoldingLog", ignore = true)
    @Mapping(target = "contractor", ignore = true)
    @Mapping(target = "contractorContact", ignore = true)
    @Mapping(target = "scaffoldingUser", ignore = true)
    @Mapping(target = "scaffoldingUserContact", ignore = true)
    @Mapping(target = "dimensions", ignore = true)
    @Mapping(target = "workingTimes", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromDto(ScaffoldingLogPositionDTO dto, @MappingTarget ScaffoldingLogPosition entity);
}
