package pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ScaffoldingLogPositionWorkingTimeFilter {

    private Long id;
    private BigDecimal numberOfWorkersGreaterOrEqual;
    private BigDecimal numberOfWorkersLesserOrEqual;
    private BigDecimal numberOfHoursGreaterOrEqual;
    private BigDecimal numberOfHoursLesserOrEqual;
}
