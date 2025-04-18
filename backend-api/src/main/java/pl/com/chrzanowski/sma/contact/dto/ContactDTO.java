package pl.com.chrzanowski.sma.contact.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContactDTO extends ContactBaseDTO {
    private ContractorBaseDTO contractor;
    private CompanyBaseDTO company;
}
