package pl.com.chrzanowski.sma.contact.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

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
    protected final Instant createdDatetime;
    protected final Instant lastModifiedDatetime;
    protected final Long createdById;
    protected final String createdByFirstName;
    protected final String createdByLastName;
    protected final Long modifiedById;
    protected final String modifiedByFirstName;
    protected final String modifiedByLastName;
}
