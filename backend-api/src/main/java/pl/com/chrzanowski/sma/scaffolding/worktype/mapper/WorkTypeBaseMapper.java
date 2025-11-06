package pl.com.chrzanowski.sma.scaffolding.worktype.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;

@Mapper(componentModel = "spring")
public interface WorkTypeBaseMapper extends EntityMapper<WorkTypeBaseDTO, WorkType> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    WorkType toEntity(WorkTypeBaseDTO workTypeBaseDTO);

    WorkTypeBaseDTO toDto(WorkType workType);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(WorkTypeBaseDTO workTypeBaseDTO, @MappingTarget WorkType workType);
}
