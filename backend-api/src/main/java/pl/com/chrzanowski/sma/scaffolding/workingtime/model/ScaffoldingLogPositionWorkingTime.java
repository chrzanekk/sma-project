package pl.com.chrzanowski.sma.scaffolding.workingtime.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.unit.model.Unit;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scaffolding_log_position_working_time")
public class ScaffoldingLogPositionWorkingTime extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scaffolding_log_position_working_time_seq")
    @SequenceGenerator(name = "scaffolding_log_position_working_time_seq",
            sequenceName = "scaffolding_log_position_working_time_sequence",
            allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "number_of_workers")
    @DecimalMin(value = "0.0")
    private Integer numberOfWorkers;

    @Column(name = "number_of_hours", precision = 10, scale = 2)
    @DecimalMin(value = "0.0")
    private BigDecimal numberOfHours;

    @Column(name = "operation_type", length = 50, nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ScaffoldingOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scaffolding_position_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private ScaffoldingLogPosition scaffoldingPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Company company;
}
