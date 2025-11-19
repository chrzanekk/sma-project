package pl.com.chrzanowski.sma.unit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.company.model.Company;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "units")
public class Unit extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_seq")
    @SequenceGenerator(name = "unit_seq", sequenceName = "unit_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "symbol")
    @NotBlank
    private String symbol;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @ToString.Exclude
    private Company company;
}
