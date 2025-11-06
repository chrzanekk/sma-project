package pl.com.chrzanowski.sma.scaffolding.worktype.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;

@Mapper(componentModel = "spring", uses = {WorkTypeBaseMapper.class, CompanyBaseMapper.class})
public interface WorkTypeDTOMapper extends EntityMapper<WorkTypeDTO, WorkType> {


    @Mapping(source = "company", target = "company")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    WorkTypeDTO toDto(WorkType workType);

    @Mapping(source = "company", target = "company")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    WorkType toEntity(WorkTypeDTO workTypeDTO);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(WorkTypeDTO workTypeDTO, @MappingTarget WorkType workType);



}
