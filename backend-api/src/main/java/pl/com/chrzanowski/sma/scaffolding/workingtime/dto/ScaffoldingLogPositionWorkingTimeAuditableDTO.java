package pl.com.chrzanowski.sma.scaffolding.workingtime.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.audit.AuditableDTO;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class ScaffoldingLogPositionWorkingTimeAuditableDTO extends AuditableDTO {
    @JsonUnwrapped
    private ScaffoldingLogPositionWorkingTimeDTO base;
}
