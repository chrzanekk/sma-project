package pl.com.chrzanowski.sma.contractor.service;

import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;

import java.util.List;

public interface ContractorService {
    ContractorDTO save(ContractorDTO contractorDTO);

    ContractorDTO update(ContractorDTO contractorDTO);

    ContractorDTO findById(Long id);

    List<ContractorDTO> findAll();

    void delete(Long id);
}
