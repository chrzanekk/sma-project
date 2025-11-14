package pl.com.chrzanowski.sma.scaffolding.dimension.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scaffolding_log_position_dimensions")
public class ScaffoldingLogPositionDimension extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scaffolding_log_position_dimensions_seq")
    @SequenceGenerator(name = "scaffolding_log_position_dimensions_seq",
            sequenceName = "scaffolding_log_position_dimensions_sequence",
            allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "height", precision = 10, scale = 2)
    @DecimalMin(value = "0.0")
    private BigDecimal height;

    @Column(name = "width", precision = 10, scale = 2)
    @DecimalMin(value = "0.0")
    private BigDecimal width;

    @Column(name = "length", precision = 10, scale = 2)
    @DecimalMin(value = "0.0")
    private BigDecimal length;

    @Column(name = "dimension_type", length = 50, nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private DimensionType dimensionType;

    @Column(name = "operation_type", length = 50, nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ScaffoldingOperationType operationType;

    @Column(name = "dismantling_date")
    private LocalDate dismantlingDate;

    @Column(name = "assembly_date")
    private LocalDate assemblyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scaffolding_position_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private ScaffoldingLogPosition scaffoldingPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Company company;
}
