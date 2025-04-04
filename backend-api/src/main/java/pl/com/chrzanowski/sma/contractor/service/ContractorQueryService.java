package pl.com.chrzanowski.sma.contractor.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

public interface ContractorQueryService extends QueryService<ContractorDTO, ContractorFilter> {
}
