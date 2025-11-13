package pl.com.chrzanowski.sma.scaffolding.dimension.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeBaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ScaffoldingLogPositionDimensionBaseDTO {
    protected Long id;
    protected BigDecimal height;
    protected BigDecimal width;
    protected BigDecimal length;
    protected DimensionType dimensionType;
    protected LocalDate dismantlingDate;
    protected LocalDate assemblyDate;
    @NotNull
    protected WorkTypeBaseDTO workType;
}
