package pl.com.chrzanowski.sma.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.model.Company;

@Mapper(componentModel = "spring", uses = {CompanyBaseMapper.class})
public interface CompanyDTOMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "additionalInfo", source = "additionalInfo")
    CompanyDTO toDto(Company company);
}
