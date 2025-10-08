package pl.com.chrzanowski.sma.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.model.Position;

@Mapper(componentModel = "spring", uses = {PositionBaseMapper.class, CompanyBaseMapper.class})
public interface PositionDTOMapper extends EntityMapper<PositionDTO, Position> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    PositionDTO toDto(Position position);

    @Mapping(source = "company", target = "company")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Position toEntity(PositionDTO dto);

    @Mapping(source = "company", target = "company")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(PositionDTO dto, @MappingTarget Position position);
}
