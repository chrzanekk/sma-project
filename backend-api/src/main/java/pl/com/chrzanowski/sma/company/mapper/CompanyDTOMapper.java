package pl.com.chrzanowski.sma.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.model.Company;

@Mapper(componentModel = "spring", uses = {CompanyBaseMapper.class})
public interface CompanyDTOMapper extends EntityMapper<CompanyDTO, Company> {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "additionalInfo", source = "additionalInfo")
    CompanyDTO toDto(Company company);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Company toEntity(CompanyDTO companyDTO);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(CompanyDTO companyDTO, @MappingTarget Company company);
}
