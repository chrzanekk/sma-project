package pl.com.chrzanowski.sma.scaffolding.worktype.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, WorkTypeDTOMapper.class, CompanyDTOMapper.class})
public interface WorkTypeAuditMapper extends EntityMapper<WorkTypeAuditableDTO, WorkType> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "name", target = "base.name")
    @Mapping(source = "description", target = "base.description")
    @Mapping(source = "company", target = "base.company")
    WorkTypeAuditableDTO toDto(WorkType workType);
}
