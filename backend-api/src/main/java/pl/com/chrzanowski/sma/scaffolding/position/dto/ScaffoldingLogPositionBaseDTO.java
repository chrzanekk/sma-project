package pl.com.chrzanowski.sma.scaffolding.position.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingType;
import pl.com.chrzanowski.sma.common.enumeration.TechnicalProtocolStatus;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ScaffoldingLogPositionBaseDTO {
    protected Long id;
    protected String scaffoldingNumber;
    protected String assemblyLocation;
    protected LocalDate assemblyDate;
    protected LocalDate dismantlingDate;
    protected LocalDate dismantlingNotificationDate;
    protected ScaffoldingType scaffoldingType;
    protected BigDecimal scaffoldingFullDimension;
    protected TechnicalProtocolStatus technicalProtocolStatus;
    protected ScaffoldingLogPositionBaseDTO parentPosition;
    protected Set<ScaffoldingLogPositionBaseDTO> childPositions;
    protected ScaffoldingLogBaseDTO scaffoldingLog;
}
