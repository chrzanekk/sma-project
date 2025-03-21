package pl.com.chrzanowski.sma.company.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyBaseDTO, Company> {

    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}