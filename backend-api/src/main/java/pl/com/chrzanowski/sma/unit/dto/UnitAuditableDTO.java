package pl.com.chrzanowski.sma.unit.dto;

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
public class UnitAuditableDTO extends AuditableDTO {

    @JsonUnwrapped
    private UnitDTO base;
}
