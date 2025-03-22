package pl.com.chrzanowski.sma.company.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.audit.AuditableEntity;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Table(name="company",uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Company extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(name = "company_seq", sequenceName = "company_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name="name", length=200)
    private String name;

    @Column(name = "additional_info", length = 300)
    private String additionalInfo;

    @ManyToMany(mappedBy = "companies",fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<User> users;
}
