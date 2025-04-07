package pl.com.chrzanowski.sma.common.audit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.user.dto.UserAuditDTO;

import java.time.Instant;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class AuditableDTO {
    protected Instant createdDatetime;
    protected Instant lastModifiedDatetime;
    protected UserAuditDTO createdBy;
    protected UserAuditDTO modifiedBy;
}
