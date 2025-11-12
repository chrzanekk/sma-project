package pl.com.chrzanowski.sma.scaffolding.log.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogFilter;

public interface ScaffoldingLogQueryService extends QueryService<
        ScaffoldingLogAuditableDTO,
        ScaffoldingLogFilter> {
}
