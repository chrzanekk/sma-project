package pl.com.chrzanowski.sma.scaffolding.position.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingType;
import pl.com.chrzanowski.sma.common.enumeration.TechnicalProtocolStatus;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scaffolding_log_position")
public class ScaffoldingLogPosition extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scaffolding_log_position_seq")
    @SequenceGenerator(name = "scaffolding_log_position_seq", sequenceName = "scaffolding_log_position_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "scaffolding_number", length = 50, nullable = false)
    @NotBlank
    private String scaffoldingNumber;

    @Column(name = "assembly_location", length = 500)
    private String assemblyLocation;

    @Column(name = "assembly_date")
    private LocalDate assemblyDate;

    @Column(name = "dismantling_date")
    private LocalDate dismantlingDate;

    @Column(name = "dismantling_notification_date")
    private LocalDate dismantlingNotificationDate;

    @Column(name = "scaffolding_type", length = 50)
    @Enumerated(EnumType.STRING)
    private ScaffoldingType scaffoldingType;

    @Column(name = "scaffolding_full_dimension", precision = 15, scale = 2)
    @DecimalMin(value = "0.0")
    private BigDecimal scaffoldingFullDimension;

    @Column(name = "technical_protocol_status", length = 50)
    @Enumerated(EnumType.STRING)
    private TechnicalProtocolStatus technicalProtocolStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scaffolding_log_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private ScaffoldingLog scaffoldingLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scaffolding_parent_id")
    @ToString.Exclude
    private ScaffoldingLogPosition parentPosition;

    @OneToMany(mappedBy = "parentPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ScaffoldingLogPosition> childPositions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Contractor contractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_contact_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Contact contractorContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scaffolding_user_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Contractor scaffoldingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scaffolding_user_contact_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Contact scaffoldingUserContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Company company;

    @OneToMany(mappedBy = "scaffoldingPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ScaffoldingLogPositionDimension> dimensions = new HashSet<>();

    @OneToMany(mappedBy = "scaffoldingPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ScaffoldingLogPositionWorkingTime> workingTimes = new HashSet<>();

    public void addDimension(ScaffoldingLogPositionDimension dimension) {
        dimensions.add(dimension);
        dimension.setScaffoldingPosition(this);
    }

    public void removeDimension(ScaffoldingLogPositionDimension dimension) {
        dimensions.remove(dimension);
        dimension.setScaffoldingPosition(null);
    }

    public void addWorkingTime(ScaffoldingLogPositionWorkingTime workingTime) {
        workingTimes.add(workingTime);
        workingTime.setScaffoldingPosition(this);
    }

    public void removeWorkingTime(ScaffoldingLogPositionWorkingTime workingTime) {
        workingTimes.remove(workingTime);
        workingTime.setScaffoldingPosition(null);
    }

    public void addChildPosition(ScaffoldingLogPosition childPosition) {
        childPositions.add(childPosition);
        childPosition.setParentPosition(this);
    }

    public void removeChildPosition(ScaffoldingLogPosition childPosition) {
        childPositions.remove(childPosition);
        childPosition.setParentPosition(null);
    }
}
