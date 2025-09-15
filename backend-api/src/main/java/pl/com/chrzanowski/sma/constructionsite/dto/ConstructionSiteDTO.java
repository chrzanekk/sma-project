package pl.com.chrzanowski.sma.constructionsite.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contracts.dto.ContractBaseDTO;

import java.util.List;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConstructionSiteDTO extends ConstructionSiteBaseDTO{
    CompanyBaseDTO company;
    List<ContractorBaseDTO> contractors;
    List<ContractBaseDTO> contracts;
}
