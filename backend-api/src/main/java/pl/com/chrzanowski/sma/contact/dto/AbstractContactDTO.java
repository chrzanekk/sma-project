package pl.com.chrzanowski.sma.contact.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.user.dto.UserDTO;

import java.time.Instant;

@Getter
@SuperBuilder
public abstract class AbstractContactDTO {
    protected final Long id;
    protected final String firstName;
    protected final String lastName;
    protected final String phoneNumber;
    protected final String email;
    protected final String additionalInfo;
    protected final CompanyBaseDTO company;
    protected final Instant createdDatetime;
    protected final Instant lastModifiedDatetime;
    protected final UserDTO createdBy;
    protected final UserDTO modifiedBy;
}
