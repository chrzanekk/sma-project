package pl.com.chrzanowski.sma.scaffolding.log.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scaffolding_log")
public class ScaffoldingLog extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scaffolding_log_seq")
    @SequenceGenerator(name = "scaffolding_log_seq", sequenceName = "scaffolding_log_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "additional_info", length = 500, nullable = false)
    @NotBlank
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_site_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private ConstructionSite constructionSite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Contractor contractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Company company;

    @OneToMany(mappedBy = "scaffoldingLog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ScaffoldingLogPosition> positions = new HashSet<>();

    public void addPosition(ScaffoldingLogPosition position) {
        positions.add(position);
        position.setScaffoldingLog(this);
    }

    public void removePosition(ScaffoldingLogPosition position) {
        positions.remove(position);
        position.setScaffoldingLog(null);
    }
}
