package pl.com.chrzanowski.sma.constructionsitecontractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;

import java.util.List;

@Repository
public interface ConstructionSiteContractorRepository extends JpaRepository<ConstructionSiteContractor, ConstructionSiteContractorId> {

    // Przykładowa metoda wyszukiwania po constructionSiteId
    List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId);

    // Przykładowa metoda wyszukiwania po contractorId
    List<ConstructionSiteContractor> findByIdContractorId(Long contractorId);
}
