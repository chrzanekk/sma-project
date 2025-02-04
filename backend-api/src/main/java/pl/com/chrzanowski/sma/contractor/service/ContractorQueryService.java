package pl.com.chrzanowski.sma.contractor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

import java.util.List;

public interface ContractorQueryService {

    List<ContractorDTO> findByFilter(ContractorFilter contractorFilter);

    Page<ContractorDTO> findByFilter(ContractorFilter contractorFilter, Pageable pageable);

}
