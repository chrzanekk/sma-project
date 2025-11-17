package pl.com.chrzanowski.sma.scaffolding.workingtime.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;

import java.math.BigDecimal;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ScaffoldingLogPositionWorkingTimeBaseDTO {
    protected Long id;
    protected Integer numberOfWorkers;
    protected BigDecimal numberOfHours;
    protected ScaffoldingOperationType operationType;

}
