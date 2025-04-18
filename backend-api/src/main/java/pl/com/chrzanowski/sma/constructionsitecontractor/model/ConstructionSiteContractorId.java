package pl.com.chrzanowski.sma.constructionsitecontractor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConstructionSiteContractorId {
    @Column(name = "contractor_id")
    private Long contractorId;

    @Column(name = "construction_site_id")
    private Long constructionSiteId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructionSiteContractorId that)) return false;
        return contractorId.equals(that.contractorId) &&
                constructionSiteId.equals(that.constructionSiteId);
    }

    @Override
    public int hashCode() {
        int result = contractorId.hashCode();
        result = 31 * result + constructionSiteId.hashCode();
        return result;
    }
}
