package pl.com.chrzanowski.sma.unit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UnitBaseMapper.class, CompanyBaseMapper.class, UnitDTOMapper.class, UserAuditMapper.class})
public interface UnitDTOMapper extends EntityMapper<UnitDTO, Unit> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "description", target = "description")
    UnitDTO toDto(Unit position);

    @Mapping(source = "company", target = "company")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Unit toEntity(UnitDTO dto);

    @Mapping(source = "company", target = "company")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(UnitDTO dto, @MappingTarget Unit position);
}
