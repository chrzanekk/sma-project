package pl.com.chrzanowski.sma.company.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@SuperBuilder
@NoArgsConstructor(force = true)
public abstract class AbstractCompanyDTO {

    protected Long id;
    protected String name;
    protected String additionalInfo;
    protected final Instant createdDatetime;
    protected final Instant lastModifiedDatetime;
    protected final Long createdById;
    protected final String createdByFirstName;
    protected final String createdByLastName;
    protected final Long modifiedById;
    protected final String modifiedByFirstName;
    protected final String modifiedByLastName;
}
