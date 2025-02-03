package pl.com.chrzanowski.sma.contact.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;

import java.util.Set;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContactHasContractorsDTO extends AbstractContactDTO {
    private final Set<ContractorDTO> contractors;
}
