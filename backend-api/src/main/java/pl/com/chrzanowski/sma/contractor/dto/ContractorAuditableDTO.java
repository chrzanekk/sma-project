package pl.com.chrzanowski.sma.contractor.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.audit.AuditableDTO;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ContractorAuditableDTO extends AuditableDTO {
    @JsonUnwrapped
    private ContractorDTO base;
}
