package pl.com.chrzanowski.sma.contractor.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractorUpdateDTO extends AbstractContractorDTO {
    private final Set<ContactBaseDTO> addedContacts;
    private final Set<ContactBaseDTO> deletedContacts;
}
