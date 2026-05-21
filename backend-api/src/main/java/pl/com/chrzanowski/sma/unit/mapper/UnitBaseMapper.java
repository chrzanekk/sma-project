package pl.com.chrzanowski.sma.unit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;
import pl.com.chrzanowski.sma.unit.model.Unit;

@Mapper(componentModel = "spring")
public interface UnitBaseMapper extends EntityMapper<UnitBaseDTO, Unit> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Unit toEntity(UnitBaseDTO dto);

    UnitBaseDTO toDto(Unit entity);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(UnitBaseDTO dto, @MappingTarget Unit entity);
}
