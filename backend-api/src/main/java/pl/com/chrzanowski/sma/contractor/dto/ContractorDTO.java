package pl.com.chrzanowski.sma.contractor.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractorDTO extends ContractorBaseDTO {
    protected final CompanyBaseDTO company;
    private final Set<ContactBaseDTO> contacts;
}
