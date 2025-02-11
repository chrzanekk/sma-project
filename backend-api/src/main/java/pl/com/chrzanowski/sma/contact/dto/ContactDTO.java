package pl.com.chrzanowski.sma.contact.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContactDTO extends AbstractContactDTO {
    private final Set<ContractorBaseDTO> contractors;
}
