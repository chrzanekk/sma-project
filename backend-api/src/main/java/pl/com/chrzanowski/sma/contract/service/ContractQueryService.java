package pl.com.chrzanowski.sma.contract.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.contract.dto.ContractAuditableDTO;
import pl.com.chrzanowski.sma.contract.service.filter.ContractFilter;

public interface ContractQueryService extends QueryService<ContractAuditableDTO, ContractFilter> {
}
