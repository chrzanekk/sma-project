package pl.com.chrzanowski.sma.scaffolding.log.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ScaffoldingLogFilter {

    private Long id;
    private Long companyId;
    private Long constructionSiteId;
    private Long contractorId;
    private String nameContains;
    private String additionalInfoContains;
    private String constructionSiteNameContains;
    private String contractorNameContains;

}
