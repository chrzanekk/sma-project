package pl.com.chrzanowski.sma.scaffolding.dimension.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.filter.ScaffoldingLogPositionDimensionFilter;

public interface ScaffoldingLogPositionDimensionQueryService extends QueryService<
        ScaffoldingLogPositionDimensionAuditableDTO,
        ScaffoldingLogPositionDimensionFilter> {
}
