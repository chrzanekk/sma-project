package pl.com.chrzanowski.sma.scaffolding.position.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingType;
import pl.com.chrzanowski.sma.common.enumeration.TechnicalProtocolStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ScaffoldingLogPositionFilter {

    private Long id;
    private String scaffoldingNumberContains;
    private String assemblyLocationContains;
    private LocalDate assemblyDateGreaterOrEqual;
    private LocalDate assemblyDateLessOrEqual;
    private LocalDate dismantlingDateGreaterOrEqual;
    private LocalDate dismantlingDateLessOrEqual;
    private LocalDate dismantlingNotificationDateGreaterOrEqual;
    private LocalDate dismantlingNotificationDateLessOrEqual;
    private TechnicalProtocolStatus technicalProtocolStatus;
    private ScaffoldingType scaffoldingType;
    private BigDecimal scaffoldingFullDimensionGreaterOrEqual;
    private BigDecimal scaffoldingFullDimensionLessOrEqual;
    private String scaffoldingLogNameContains;
    private String scaffoldingLogAdditionalInfoContains;
    private String contractorNameContains;
    private String contractorContactNameContains;
    private String scaffoldingUserNameContains;
    private String scaffoldingUserContactNameContains;

    //todo in future extend filter of working time (qoantuty of hours multiply quantity of workers) and dimensions maybe

}
