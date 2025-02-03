package pl.com.chrzanowski.sma.contractor.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.Set;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractorHasContactsDTO extends AbstractContractorDTO {
    private final Set<ContactDTO> contacts;
}
