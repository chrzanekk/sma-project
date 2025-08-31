package pl.com.chrzanowski.sma.constructionsite.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;

import java.util.List;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConstructionSiteCreateDTO extends ConstructionSiteBaseDTO {
    CompanyBaseDTO company;
    //todo change single object to collection
    List<ContractorBaseDTO> contractors;

}
