package pl.com.chrzanowski.sma.constructionsitecontractor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

@Entity
@Table(name = "construction_site_contractor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConstructionSiteContractor {

    @EmbeddedId
    private ConstructionSiteContractorId id;

    @MapsId("constructionSiteId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "construction_site_id")
    private ConstructionSite constructionSite;

    @MapsId("contractorId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    public ConstructionSiteContractor(ConstructionSite site, Contractor contractor) {
        this.constructionSite = site;
        this.contractor = contractor;
        this.id = new ConstructionSiteContractorId(site.getId(), contractor.getId());
    }
}
