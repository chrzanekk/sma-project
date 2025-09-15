package pl.com.chrzanowski.sma.contracts.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.contracts.dto.ContractAuditableDTO;
import pl.com.chrzanowski.sma.contracts.service.filter.ContractFilter;

public interface ContractQueryService extends QueryService<ContractAuditableDTO, ContractFilter> {
}
