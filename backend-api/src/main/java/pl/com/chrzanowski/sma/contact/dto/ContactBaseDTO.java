package pl.com.chrzanowski.sma.contact.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ContactBaseDTO {
    protected final Long id;
    protected final String firstName;
    protected final String lastName;
    protected final String phoneNumber;
    protected final String email;
    protected final String additionalInfo;
}