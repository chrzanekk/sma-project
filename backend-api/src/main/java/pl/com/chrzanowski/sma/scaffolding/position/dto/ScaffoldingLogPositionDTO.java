package pl.com.chrzanowski.sma.scaffolding.position.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeBaseDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScaffoldingLogPositionDTO extends ScaffoldingLogPositionBaseDTO implements HasId<Long> {
    @NotNull
    private CompanyBaseDTO company;

}
