package pl.com.chrzanowski.sma.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.position.dto.PositionAuditableDTO;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, PositionDTOMapper.class})
public interface PositionAuditMapper extends EntityMapper<PositionAuditableDTO, Position> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "name", target = "base.name")
    @Mapping(source = "description", target = "base.description")
    PositionAuditableDTO toDto(Position position);

}
