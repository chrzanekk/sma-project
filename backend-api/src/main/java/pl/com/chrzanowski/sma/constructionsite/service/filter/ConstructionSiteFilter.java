package pl.com.chrzanowski.sma.constructionsite.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ConstructionSiteFilter {
    private Long id;
    private String nameStartsWith;
    private String addressStartsWith;
    private String shortNameStartsWith;
    private String codeStartsWith;
    private String countryCode;
    private Long companyId;
    private String contractorNameStartsWith;
}
