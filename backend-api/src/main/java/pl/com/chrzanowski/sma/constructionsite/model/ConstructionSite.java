package pl.com.chrzanowski.sma.constructionsite.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "construction_site")
public class ConstructionSite extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "construction_site_seq")
    @SequenceGenerator(name = "constr_site_seq", sequenceName = "construction_site_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "code", length = 3)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "constructionSite",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ConstructionSiteContractor> siteContractors = new HashSet<>();
}
