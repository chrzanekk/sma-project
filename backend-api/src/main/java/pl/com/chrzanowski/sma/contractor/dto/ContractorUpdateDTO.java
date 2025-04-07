package pl.com.chrzanowski.sma.contractor.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractorUpdateDTO extends ContractorDTO {
    private final Set<ContactDTO> addedContacts;
    private final Set<ContactDTO> deletedContacts;
}
