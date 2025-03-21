package pl.com.chrzanowski.sma.company.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;

public interface CompanyQueryService extends QueryService<CompanyBaseDTO, CompanyFilter> {
}
