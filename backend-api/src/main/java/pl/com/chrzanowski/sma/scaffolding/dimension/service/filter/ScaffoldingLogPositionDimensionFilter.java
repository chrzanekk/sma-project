package pl.com.chrzanowski.sma.scaffolding.dimension.service.filter;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ScaffoldingLogPositionDimensionFilter {

    private Long id;
    private Long scaffoldingPositionId;
    private BigDecimal heightGreaterOrEqual;
    private BigDecimal heightLesserOrEqual;
    private BigDecimal widthGreaterOrEqual;
    private BigDecimal widthLesserOrEqual;
    private BigDecimal lengthGreaterOrEqual;
    private BigDecimal lengthLesserOrEqual;
    private DimensionType dimensionType;
    private LocalDate dismantlingDateGreaterOrEqual;
    private LocalDate dismantlingDateLesserOrEqual;
}
