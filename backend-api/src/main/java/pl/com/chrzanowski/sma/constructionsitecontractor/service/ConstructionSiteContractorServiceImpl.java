package pl.com.chrzanowski.sma.constructionsitecontractor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;
import pl.com.chrzanowski.sma.constructionsitecontractor.repository.ConstructionSiteContractorRepository;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorDTOMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConstructionSiteContractorServiceImpl implements ConstructionSiteContractorService {

    private final ConstructionSiteContractorRepository constructionSiteContractorRepository;
    private final ContractorDTOMapper contractorDTOMapper;

    public ConstructionSiteContractorServiceImpl(ConstructionSiteContractorRepository constructionSiteContractorRepository, ContractorDTOMapper contractorDTOMapper) {
        this.constructionSiteContractorRepository = constructionSiteContractorRepository;
        this.contractorDTOMapper = contractorDTOMapper;
    }

    @Override
    @Transactional
    public ConstructionSiteContractor save(ConstructionSiteContractor constructionSiteContractor) {
        return constructionSiteContractorRepository.save(constructionSiteContractor);
    }

    @Override
    @Transactional
    public Optional<ConstructionSiteContractor> findById(ConstructionSiteContractorId id) {
        return constructionSiteContractorRepository.findById(id);
    }

    @Override
    public Page<ContractorDTO> findAllContractorsByConstructionSiteIdPaged(Long constructionSiteId, Pageable pageable) {
        Page<Contractor> contractorsPage = constructionSiteContractorRepository
                .findContractorsByConstructionSiteId(constructionSiteId, pageable);
        return contractorsPage.map(contractorDTOMapper::toDto);
    }

    @Override
    @Transactional
    public List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId) {
        return constructionSiteContractorRepository.findByIdConstructionSiteId(constructionSiteId);
    }

    @Override
    @Transactional
    public List<ConstructionSiteContractor> findByIdContractorId(Long contractorSiteId) {
        return constructionSiteContractorRepository.findByIdContractorId(contractorSiteId);
    }

    @Override
    @Transactional
    public void deleteById(ConstructionSiteContractorId id) {
        constructionSiteContractorRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteByIdConstructionSiteId(Long constructionSiteId) {
        constructionSiteContractorRepository.deleteByIdConstructionSiteId(constructionSiteId);
    }

    @Override
    public void deleteByIdContractorId(Long contractorId) {
        constructionSiteContractorRepository.deleteByIdContractorId(contractorId);
    }

    @Override
    public void deleteByConstructionSiteIdAndContractorId(Long constructionSiteId, Long contractorId) {
        constructionSiteContractorRepository.deleteByConstructionSiteIdAndContractorId(constructionSiteId, contractorId);
    }
}
