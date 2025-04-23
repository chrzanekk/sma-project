package pl.com.chrzanowski.sma.constructionsitecontractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;

import java.util.List;

@Repository
public interface ConstructionSiteContractorRepository extends JpaRepository<ConstructionSiteContractor, ConstructionSiteContractorId> {

    List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId);

    List<ConstructionSiteContractor> findByIdContractorId(Long contractorId);

    void deleteByIdConstructionSiteId(Long constructionSiteId);

    void deleteByIdContractorId(Long constructionSiteId);
}
