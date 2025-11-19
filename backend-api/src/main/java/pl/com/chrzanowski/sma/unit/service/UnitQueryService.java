package pl.com.chrzanowski.sma.unit.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.unit.dto.UnitAuditableDTO;
import pl.com.chrzanowski.sma.unit.service.filter.UnitFilter;

public interface UnitQueryService extends QueryService<UnitAuditableDTO, UnitFilter> {
}
