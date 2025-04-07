package pl.com.chrzanowski.sma.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring")
public interface CompanyBaseMapper extends EntityMapper<CompanyBaseDTO, Company> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromBaseDto(CompanyBaseDTO dto, @MappingTarget Company company);

    CompanyBaseDTO toDto(Company company);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Company toEntity(CompanyBaseDTO dto);
}