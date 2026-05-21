package pl.com.chrzanowski.sma.scaffolding.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.log.mapper.ScaffoldingLogBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeBaseMapper;
import pl.com.chrzanowski.sma.unit.mapper.UnitBaseMapper;

@Mapper(componentModel = "spring", uses = {
        ScaffoldingLogBaseMapper.class,
        UnitBaseMapper.class,
        ScaffoldingLogPositionDimensionBaseMapper.class,
        ScaffoldingLogPositionWorkingTimeBaseMapper.class,
        ContractorBaseMapper.class,
        ContactBaseMapper.class,
        UnitBaseMapper.class})
public interface ScaffoldingLogPositionBaseMapper extends EntityMapper<ScaffoldingLogPositionBaseDTO, ScaffoldingLogPosition> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ScaffoldingLogPosition toEntity(ScaffoldingLogPositionBaseDTO dto);

    @Mapping(target = "childPositions", ignore = true)
    @Mapping(target = "parentPosition", ignore = true)
    ScaffoldingLogPositionBaseDTO toDto(ScaffoldingLogPosition entity);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ScaffoldingLogPositionBaseDTO dto, @MappingTarget ScaffoldingLogPosition entity);
}
