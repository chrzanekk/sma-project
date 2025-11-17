package pl.com.chrzanowski.sma.scaffolding.dimension.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected BigDecimal height;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected BigDecimal width;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected BigDecimal length;
    protected DimensionType dimensionType;
    protected LocalDate dismantlingDate;
    protected LocalDate assemblyDate;
    protected ScaffoldingOperationType operationType;
}
