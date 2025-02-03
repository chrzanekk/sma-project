package pl.com.chrzanowski.sma.contact.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.model.AuditableEntity;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_seq")
    @SequenceGenerator(name = "contact_seq", sequenceName = "contacts_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "additional_info", length = 250)
    private String additionalInfo;

    @ManyToMany(mappedBy = "contacts", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Contractor> contractors;
}
