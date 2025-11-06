package pl.com.chrzanowski.sma.scaffolding.worktype.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeFilter;

public interface WorkTypeQueryService extends QueryService<WorkTypeAuditableDTO, WorkTypeFilter> {
}
