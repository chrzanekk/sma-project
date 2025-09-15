package pl.com.chrzanowski.sma.contractor.service;

import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorUpdateDTO;

public interface ContractorService extends BaseCrudService<ContractorDTO, ContractorDTO, ContractorDTO, Long> {

    ContractorDTO updateWithChangedContacts(ContractorUpdateDTO updateDTO);
}
