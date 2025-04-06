package pl.com.chrzanowski.sma.company.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.user.dto.UserAuditDTO;

import java.time.Instant;

@Getter
@SuperBuilder
@NoArgsConstructor(force = true)
public abstract class AbstractCompanyDTO {

    protected Long id;
    protected String name;
    protected String additionalInfo;
    protected UserAuditDTO createdBy;
    protected UserAuditDTO modifiedBy;
    protected final Instant createdDatetime;
    protected final Instant lastModifiedDatetime;
}
