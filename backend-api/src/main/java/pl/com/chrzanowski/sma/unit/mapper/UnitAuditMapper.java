package pl.com.chrzanowski.sma.unit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.unit.dto.UnitAuditableDTO;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, UnitDTOMapper.class})
public interface UnitAuditMapper extends EntityMapper<UnitAuditableDTO, Unit> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "symbol", target = "base.symbol")
    @Mapping(source = "description", target = "base.description")
    UnitAuditableDTO toDto(Unit position);

}
