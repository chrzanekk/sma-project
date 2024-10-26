package pl.com.chrzanowski.sma.contractor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;

import java.util.List;

public interface ContractorService {
    ContractorDTO save(ContractorDTO contractorDTO);

    ContractorDTO update(ContractorDTO contractorDTO);

    List<ContractorDTO> findByFilter(ContractorFilter contractorFilter);

    Page<ContractorDTO> findByFilterAndPage(ContractorFilter contractorFilter, Pageable pageable);

    ContractorDTO findById(Long id);

    List<ContractorDTO> findAll();

    void delete(Long id);
}
