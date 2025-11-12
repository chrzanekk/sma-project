package pl.com.chrzanowski.sma.scaffolding.position.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingType;
import pl.com.chrzanowski.sma.common.enumeration.TechnicalProtocolStatus;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeBaseDTO;

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
    @NotNull
    protected Long id;
    @NotNull
    protected String scaffoldingNumber;
    protected String assemblyLocation;
    @NotNull
    protected LocalDate assemblyDate;
    protected LocalDate dismantlingDate;
    protected LocalDate dismantlingNotificationDate;
    @NotNull
    protected ScaffoldingType scaffoldingType;
    protected BigDecimal scaffoldingFullDimension;
    @NotNull
    protected TechnicalProtocolStatus technicalProtocolStatus;
    protected ScaffoldingLogPositionBaseDTO parentPosition;
    protected Set<ScaffoldingLogPositionBaseDTO> childPositions;
    @NotNull
    protected ScaffoldingLogBaseDTO scaffoldingLog;
    @NotNull
    private ContractorBaseDTO contractor;
    @NotNull
    private ContactBaseDTO contractorContact;
    @NotNull
    private ContractorBaseDTO scaffoldingUser;
    @NotNull
    private ContactBaseDTO scaffoldingUserContact;
    private Set<ScaffoldingLogPositionDimensionBaseDTO> dimensions;
    private Set<ScaffoldingLogPositionWorkingTimeBaseDTO> workingTimes;
}
