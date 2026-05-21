package pl.com.chrzanowski.sma.contractor.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contract.dto.ContractBaseDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder(toBuilder = true)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractorDTO extends ContractorBaseDTO implements HasId<Long> {
    protected final CompanyBaseDTO company;
    private final Set<ContactBaseDTO> contacts;
    private final Set<ContractBaseDTO> contracts;
}
