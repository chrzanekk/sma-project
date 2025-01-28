package pl.com.chrzanowski.sma.contractor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.model.AuditableEntity;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contractors")
public class Contractor extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contractor_seq")
    @SequenceGenerator(name = "contractor_seq", sequenceName = "contractor_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name", length = 200)
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "building_no")
    private String buildingNo;

    @Column(name = "apartment_no")
    private String apartmentNo;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(name = "customer", nullable = false)
    private Boolean customer;

    @Column(name = "supplier", nullable = false)
    private Boolean supplier;

    @Column(name = "scaffolding_user", nullable = false)
    private Boolean scaffoldingUser;
}
