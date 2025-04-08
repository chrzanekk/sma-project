package pl.com.chrzanowski.sma.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.dto.CompanyAuditableDTO;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, CompanyDTOMapper.class})
public interface CompanyAuditMapper extends EntityMapper<CompanyAuditableDTO, Company> {

    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "name", target = "base.name")
    @Mapping(source = "additionalInfo", target = "base.additionalInfo")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    CompanyAuditableDTO toDto(Company company);

}
