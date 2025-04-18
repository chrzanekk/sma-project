package pl.com.chrzanowski.sma.constructionsitecontractor.service;

import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;
import pl.com.chrzanowski.sma.constructionsitecontractor.repository.ConstructionSiteContractorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConstructionSiteContractorServiceImpl implements ConstructionSiteContractorService {

    private final ConstructionSiteContractorRepository constructionSiteContractorRepository;

    public ConstructionSiteContractorServiceImpl(ConstructionSiteContractorRepository constructionSiteContractorRepository) {
        this.constructionSiteContractorRepository = constructionSiteContractorRepository;
    }

    @Override
    public ConstructionSiteContractor save(ConstructionSiteContractor constructionSiteContractor) {
        return constructionSiteContractorRepository.save(constructionSiteContractor);
    }

    @Override
    public Optional<ConstructionSiteContractor> findById(ConstructionSiteContractorId id) {
        return constructionSiteContractorRepository.findById(id);
    }

    @Override
    public List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId) {
        return constructionSiteContractorRepository.findByIdConstructionSiteId(constructionSiteId);
    }

    @Override
    public List<ConstructionSiteContractor> findByIdContractorId(Long contractorSiteId) {
        return constructionSiteContractorRepository.findByIdContractorId(contractorSiteId);
    }

    @Override
    public void deleteById(ConstructionSiteContractorId id) {
        constructionSiteContractorRepository.deleteById(id);
    }
}
