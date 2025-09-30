package pl.com.chrzanowski.sma.contracts.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractDTO extends ContractBaseDTO implements HasId<Long> {
    private CompanyBaseDTO company;
    private ConstructionSiteBaseDTO constructionSite;
    private ContractorBaseDTO contractor;
    private ContactBaseDTO contact;
}
