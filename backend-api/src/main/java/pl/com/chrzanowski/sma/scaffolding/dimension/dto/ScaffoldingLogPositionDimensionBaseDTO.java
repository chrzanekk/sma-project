package pl.com.chrzanowski.sma.scaffolding.dimension.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Jacksonized
@SuperBuilder(toBuilder = true)
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
    protected ScaffoldingOperationType operationType;
}
