package pl.com.chrzanowski.sma.position.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.common.exception.PositionException;
import pl.com.chrzanowski.sma.common.exception.error.PositionErrorCode;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "positions")
public class Position extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_seq")
    @SequenceGenerator(name = "position_seq", sequenceName = "positions_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @ToString.Exclude
    private Company company;

}
