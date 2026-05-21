package pl.com.chrzanowski.sma.scaffolding.workingtime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses={UserAuditMapper.class, ScaffoldingLogPositionWorkingTimeDTOMapper.class})
public interface ScaffoldingLogPositionWorkingTimeAuditMapper extends EntityMapper<ScaffoldingLogPositionWorkingTimeAuditableDTO, ScaffoldingLogPositionWorkingTime> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "numberOfWorkers", target = "base.numberOfWorkers")
    @Mapping(source = "numberOfHours", target = "base.numberOfHours")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "operationType", target = "base.operationType")
    ScaffoldingLogPositionWorkingTimeAuditableDTO toDto(ScaffoldingLogPositionWorkingTime entity);

}
