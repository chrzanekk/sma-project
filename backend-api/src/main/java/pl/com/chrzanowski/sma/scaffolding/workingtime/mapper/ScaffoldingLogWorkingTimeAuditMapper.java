package pl.com.chrzanowski.sma.scaffolding.workingtime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses={UserAuditMapper.class,ScaffoldingLogWorkingTimeDTOMapper.class})
public interface ScaffoldingLogWorkingTimeAuditMapper extends EntityMapper<ScaffoldingLogPositionWorkingTimeAuditableDTO, ScaffoldingLogPositionWorkingTime> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "numberOfWorkers", target = "base.numberOfWorkers")
    @Mapping(source = "numberOfHours", target = "base.numberOfHours")
    @Mapping(source = "scaffoldingPositions", target = "base.scaffoldingPositions")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "workType", target = "base.workType")
    ScaffoldingLogPositionWorkingTimeAuditableDTO toDto(ScaffoldingLogPositionWorkingTime entity);

}
