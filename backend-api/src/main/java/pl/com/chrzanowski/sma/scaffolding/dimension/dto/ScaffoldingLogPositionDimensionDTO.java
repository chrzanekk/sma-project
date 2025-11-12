package pl.com.chrzanowski.sma.scaffolding.dimension.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeBaseDTO;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScaffoldingLogPositionDimensionDTO extends ScaffoldingLogPositionDimensionBaseDTO implements HasId<Long> {
    @NotNull
    private WorkTypeBaseDTO workType;
    @NotNull
    private CompanyBaseDTO company;
    private ScaffoldingLogPositionBaseDTO scaffoldingPosition;
}
