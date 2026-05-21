package pl.com.chrzanowski.sma.scaffolding.workingtime.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter.ScaffoldingLogPositionWorkingTimeFilter;


public interface ScaffoldingLogPositionWorkingTimeQueryService extends QueryService<
        ScaffoldingLogPositionWorkingTimeAuditableDTO,
        ScaffoldingLogPositionWorkingTimeFilter> {
}
