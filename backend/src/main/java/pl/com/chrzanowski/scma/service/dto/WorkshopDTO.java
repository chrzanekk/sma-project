package pl.com.chrzanowski.scma.service.dto;

import pl.com.chrzanowski.scma.domain.enumeration.Country;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WorkshopDTO {
    private Long id;
    private String name;
    private String taxNumber;
    private String street;
    private String buildingNo;
    private String apartmentNo;
    private String postalCode;
    private String city;
    private Country country;
    private Set<ServiceActionTypeDTO> serviceActionTypes;
    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;

    public WorkshopDTO() {
    }

    private WorkshopDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        taxNumber = builder.taxNumber;
        street = builder.street;
        buildingNo = builder.buildingNo;
        apartmentNo = builder.apartmentNo;
        postalCode = builder.postalCode;
        city = builder.city;
        country = builder.country;
        serviceActionTypes = builder.serviceActionTypes;
        createDate = builder.createDate;
        modifyDate = builder.modifyDate;
        removeDate = builder.removeDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(WorkshopDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.taxNumber = copy.getTaxNumber();
        builder.street = copy.getStreet();
        builder.buildingNo = copy.getBuildingNo();
        builder.apartmentNo = copy.getApartmentNo();
        builder.postalCode = copy.getPostalCode();
        builder.city = copy.getCity();
        builder.country = copy.getCountry();
        builder.serviceActionTypes = copy.getServiceActionTypes();
        builder.createDate = copy.getCreateDate();
        builder.modifyDate = copy.getModifyDate();
        builder.removeDate = copy.getRemoveDate();
        return builder;
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

    public Set<ServiceActionTypeDTO> getServiceActionTypes() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkshopDTO that = (WorkshopDTO) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(taxNumber, that.taxNumber)) return false;
        if (!Objects.equals(street, that.street)) return false;
        if (!Objects.equals(buildingNo, that.buildingNo)) return false;
        if (!Objects.equals(apartmentNo, that.apartmentNo)) return false;
        if (!Objects.equals(postalCode, that.postalCode)) return false;
        if (!Objects.equals(city, that.city)) return false;
        if (country != that.country) return false;
        if (!Objects.equals(serviceActionTypes, that.serviceActionTypes))
            return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        if (!Objects.equals(modifyDate, that.modifyDate)) return false;
        return Objects.equals(removeDate, that.removeDate);
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
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WorkshopDTO{" +
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
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                '}';
    }

    public void addServiceActionType(ServiceActionTypeDTO serviceActionTypeDTO) {
        if(serviceActionTypes == null) {
            serviceActionTypes = new HashSet<>();
        }
        serviceActionTypes.add(serviceActionTypeDTO);
    }


    public static final class Builder {
        private Long id;
        private String name;
        private String taxNumber;
        private String street;
        private String buildingNo;
        private String apartmentNo;
        private String postalCode;
        private String city;
        private Country country;
        private Set<ServiceActionTypeDTO> serviceActionTypes;
        private Instant createDate;
        private Instant modifyDate;
        private Instant removeDate;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder taxNumber(String taxNumber) {
            this.taxNumber = taxNumber;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder buildingNo(String buildingNo) {
            this.buildingNo = buildingNo;
            return this;
        }

        public Builder apartmentNo(String apartmentNo) {
            this.apartmentNo = apartmentNo;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(Country country) {
            this.country = country;
            return this;
        }

        public Builder serviceActionTypes(Set<ServiceActionTypeDTO> serviceActionTypes) {
            this.serviceActionTypes = serviceActionTypes;
            return this;
        }

        public Builder createDate(Instant createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder modifyDate(Instant modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public Builder removeDate(Instant removeDate) {
            this.removeDate = removeDate;
            return this;
        }

        public WorkshopDTO build() {
            return new WorkshopDTO(this);
        }
    }
}
