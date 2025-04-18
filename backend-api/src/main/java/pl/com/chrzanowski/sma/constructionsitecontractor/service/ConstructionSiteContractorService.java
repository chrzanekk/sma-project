package pl.com.chrzanowski.sma.constructionsitecontractor.service;

import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;

import java.util.List;
import java.util.Optional;

public interface ConstructionSiteContractorService {

    ConstructionSiteContractor save(ConstructionSiteContractor constructionSiteContractor);

    Optional<ConstructionSiteContractor> findById(ConstructionSiteContractorId id);

    List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId);

    List<ConstructionSiteContractor> findByIdContractorId(Long contractorSiteId);

    void deleteById(ConstructionSiteContractorId id);
}
