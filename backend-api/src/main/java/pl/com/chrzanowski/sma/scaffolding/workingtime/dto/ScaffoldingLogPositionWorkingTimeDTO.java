package pl.com.chrzanowski.sma.scaffolding.workingtime.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScaffoldingLogPositionWorkingTimeDTO extends ScaffoldingLogPositionWorkingTimeBaseDTO implements HasId<Long> {
    @NotNull
    private CompanyBaseDTO company;
    private ScaffoldingLogPositionBaseDTO scaffoldingPosition;
}
