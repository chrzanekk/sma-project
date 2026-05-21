package pl.com.chrzanowski.sma.scaffolding.position.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;

@Jacksonized
@SuperBuilder(toBuilder = true)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScaffoldingLogPositionDTO extends ScaffoldingLogPositionBaseDTO implements HasId<Long> {
    @NotNull
    private CompanyBaseDTO company;

}
