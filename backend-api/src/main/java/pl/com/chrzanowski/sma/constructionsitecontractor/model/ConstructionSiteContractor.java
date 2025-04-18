package pl.com.chrzanowski.sma.constructionsitecontractor.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "construction_site_contractor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConstructionSiteContractor {

    @EmbeddedId
    private ConstructionSiteContractorId id;

    public ConstructionSiteContractor(Long contractorId, Long constructionSiteId) {
        this.id = new ConstructionSiteContractorId(contractorId, constructionSiteId);
    }
}
