package pl.com.chrzanowski.sma.constructionsitecontractor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.List;

@Repository
public interface ConstructionSiteContractorRepository extends JpaRepository<ConstructionSiteContractor, ConstructionSiteContractorId> {

    List<ConstructionSiteContractor> findByIdConstructionSiteId(Long constructionSiteId);

    List<ConstructionSiteContractor> findByIdContractorId(Long contractorId);

    void deleteByIdConstructionSiteId(Long constructionSiteId);

    void deleteByIdContractorId(Long constructionSiteId);

    void deleteByConstructionSiteIdAndContractorId(Long constructionSiteId, Long contractorId);

    @Query("""
                SELECT c FROM Contractor c
                WHERE c.id IN (
                    SELECT cs.contractor.id FROM ConstructionSiteContractor cs
                    WHERE cs.constructionSite.id = :constructionSiteId
                )
            """)
    Page<Contractor> findContractorsByConstructionSiteId(@Param("constructionSiteId") Long constructionSiteId, Pageable pageable);
}
