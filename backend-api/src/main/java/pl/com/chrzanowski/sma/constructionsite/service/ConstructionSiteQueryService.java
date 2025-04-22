package pl.com.chrzanowski.sma.constructionsite.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteFilter;

public interface ConstructionSiteQueryService extends QueryService<ConstructionSiteAuditableDTO, ConstructionSiteFilter> {
}
