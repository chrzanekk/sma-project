package pl.com.chrzanowski.sma.scaffolding.position.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionFilter;

public interface ScaffoldingLogPositionQueryService extends QueryService<ScaffoldingLogPositionAuditableDTO, ScaffoldingLogPositionFilter> {
}
