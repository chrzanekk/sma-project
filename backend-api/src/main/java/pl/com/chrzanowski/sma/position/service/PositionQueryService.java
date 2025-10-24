package pl.com.chrzanowski.sma.position.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.position.dto.PositionAuditableDTO;
import pl.com.chrzanowski.sma.position.service.filter.PositionFilter;

public interface PositionQueryService extends QueryService<PositionAuditableDTO, PositionFilter> {
}
