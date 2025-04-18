package pl.com.chrzanowski.sma.constructionsite.dto;

import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;

public record ConstructionSiteDTO(
        Long id,
        String name,
        String address,
        Country country,
        String shortName,
        String code,
        CompanyBaseDTO companyBaseDTO
) {
}
