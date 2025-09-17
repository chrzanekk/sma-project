package pl.com.chrzanowski.sma.contracts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contracts")
public class Contract extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
    @SequenceGenerator(name = "contract_seq", sequenceName = "contracts_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "number", length = 150)
    @NotBlank
    private String number;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "value")
    @NotNull
    private BigDecimal value;

    @Column(name = "currency", length = 3)
    @NotBlank
    private Currency currency;

    @Column(name = "start_date")
    @NotNull
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "signup_date")
    private Instant signupDate;

    @Column(name = "real_end_date")
    private Instant realEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_site_id")
    private ConstructionSite constructionSite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @ToString.Exclude
    private Company company;

}
