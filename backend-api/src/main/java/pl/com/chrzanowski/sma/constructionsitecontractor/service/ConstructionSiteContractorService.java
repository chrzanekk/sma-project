package pl.com.chrzanowski.sma.constructionsitecontractor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;

import java.util.List;
import java.util.Optional;

public interface ConstructionSiteContractorService {

    ConstructionSiteContractor save(ConstructionSiteContractor constructionSiteContractor);

    Optional<ConstructionSiteContractor> findById(ConstructionSiteContractorId id);

    List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId);

    Page<ContractorDTO> findAllContractorsByConstructionSiteIdPaged(Long constructionSiteId, Pageable pageable);

    List<ConstructionSiteContractor> findByIdContractorId(Long contractorSiteId);

    void deleteById(ConstructionSiteContractorId id);

    void deleteByIdConstructionSiteId(Long constructionSiteId);

    void deleteByIdContractorId(Long constructionSiteId);

    void deleteByConstructionSiteIdAndContractorId(Long constructionSiteId, Long contractorId);
}
