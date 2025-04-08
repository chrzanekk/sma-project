package pl.com.chrzanowski.sma.company.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.company.dto.CompanyAuditableDTO;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;

public interface CompanyQueryService extends QueryService<CompanyAuditableDTO, CompanyFilter> {

    CompanyDTO findByName(String name);

}
