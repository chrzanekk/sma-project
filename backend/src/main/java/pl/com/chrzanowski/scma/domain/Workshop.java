package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.com.chrzanowski.scma.domain.enumeration.Country;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "workshops")
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "workshop_service_action_type",
            joinColumns = @JoinColumn(name = "workshop_id"),
            inverseJoinColumns = @JoinColumn(name = "service_action_type_id"))
    private Set<ServiceActionType> serviceActionTypes = new HashSet<>();

    @OneToMany
    List<ServiceAction> serviceActions = new ArrayList<>();
    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;

    public Workshop() {
    }

    public Workshop(Long id,
                    String name,
                    String taxNumber,
                    String street,
                    String buildingNo,
                    String apartmentNo,
                    String postalCode,
                    String city,
                    Country country,
                    Set<ServiceActionType> serviceActionTypes,
                    List<ServiceAction> serviceActions,
                    Instant createDate,
                    Instant modifyDate,
                    Instant removeDate) {
        this.id = id;
        this.name = name;
        this.taxNumber = taxNumber;
        this.street = street;
        this.buildingNo = buildingNo;
        this.apartmentNo = apartmentNo;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.serviceActionTypes = serviceActionTypes;
        this.serviceActions = serviceActions;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public Set<ServiceActionType> getServiceActionTypes() {
        return serviceActionTypes;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public Instant getRemoveDate() {
        return removeDate;
    }

    public List<ServiceAction> getServiceActions() {
        return serviceActions;
    }

    public Workshop setId(Long id) {
        this.id = id;
        return this;
    }

    public Workshop setName(String name) {
        this.name = name;
        return this;
    }

    public Workshop setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
        return this;
    }

    public Workshop setStreet(String street) {
        this.street = street;
        return this;
    }

    public Workshop setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
        return this;
    }

    public Workshop setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
        return this;
    }

    public Workshop setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Workshop setCity(String city) {
        this.city = city;
        return this;
    }

    public Workshop setCountry(Country country) {
        this.country = country;
        return this;
    }

    public Workshop setServiceActionTypes(Set<ServiceActionType> serviceActionTypes) {
        this.serviceActionTypes = serviceActionTypes;
        return this;
    }

    public Workshop setCreateDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public Workshop setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public Workshop setRemoveDate(Instant removeDate) {
        this.removeDate = removeDate;
        return this;
    }

    public Workshop setServiceActions(List<ServiceAction> serviceActions) {
        this.serviceActions = serviceActions;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workshop workshop = (Workshop) o;

        if (!Objects.equals(id, workshop.id)) return false;
        if (!Objects.equals(name, workshop.name)) return false;
        if (!Objects.equals(taxNumber, workshop.taxNumber)) return false;
        if (!Objects.equals(street, workshop.street)) return false;
        if (!Objects.equals(buildingNo, workshop.buildingNo)) return false;
        if (!Objects.equals(apartmentNo, workshop.apartmentNo))
            return false;
        if (!Objects.equals(postalCode, workshop.postalCode)) return false;
        if (!Objects.equals(city, workshop.city)) return false;
        if (country != workshop.country) return false;
        if (!Objects.equals(serviceActionTypes, workshop.serviceActionTypes))
            return false;
        if (!Objects.equals(serviceActions, workshop.serviceActions))
            return false;
        if (!Objects.equals(createDate, workshop.createDate)) return false;
        if (!Objects.equals(modifyDate, workshop.modifyDate)) return false;
        return Objects.equals(removeDate, workshop.removeDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (taxNumber != null ? taxNumber.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (buildingNo != null ? buildingNo.hashCode() : 0);
        result = 31 * result + (apartmentNo != null ? apartmentNo.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (serviceActionTypes != null ? serviceActionTypes.hashCode() : 0);
        result = 31 * result + (serviceActions != null ? serviceActions.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Workshop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", street='" + street + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", apartmentNo='" + apartmentNo + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country=" + country +
                ", serviceActionTypes=" + serviceActionTypes +
                ", serviceActions=" + serviceActions +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                '}';
    }
}
