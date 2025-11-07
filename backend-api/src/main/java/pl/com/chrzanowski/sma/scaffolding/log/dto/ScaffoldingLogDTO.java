package pl.com.chrzanowski.sma.scaffolding.log.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScaffoldingLogDTO extends ScaffoldingLogBaseDTO implements HasId<Long> {
    @NotNull
    private CompanyBaseDTO company;
    @NotNull
    private ConstructionSiteBaseDTO constructionSite;
    @NotNull
    private ContractorBaseDTO contractor;

    private Set<ScaffoldingLogPositionBaseDTO> positions;
}
