package pl.com.chrzanowski.sma.workshop;

import pl.com.chrzanowski.sma.common.enumeration.Country;

import java.time.Instant;

public class WorkshopFilter {
    private Long id;
    private String name;
    private String taxNumber;
    private String street;
    private String buildingNo;
    private String apartmentNo;
    private String postalCode;
    private String city;
    private Country country;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;

    public WorkshopFilter() {
    }

    private WorkshopFilter(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setTaxNumber(builder.taxNumber);
        setStreet(builder.street);
        setBuildingNo(builder.buildingNo);
        setApartmentNo(builder.apartmentNo);
        setPostalCode(builder.postalCode);
        setCity(builder.city);
        setCountry(builder.country);
        setCreateDateStartWith(builder.createDateStartWith);
        setCreateDateEndWith(builder.createDateEndWith);
        setModifyDateStartWith(builder.modifyDateStartWith);
        setModifyDateEndWith(builder.modifyDateEndWith);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Instant getCreateDateStartWith() {
        return createDateStartWith;
    }

    public void setCreateDateStartWith(Instant createDateStartWith) {
        this.createDateStartWith = createDateStartWith;
    }

    public Instant getCreateDateEndWith() {
        return createDateEndWith;
    }

    public void setCreateDateEndWith(Instant createDateEndWith) {
        this.createDateEndWith = createDateEndWith;
    }

    public Instant getModifyDateStartWith() {
        return modifyDateStartWith;
    }

    public void setModifyDateStartWith(Instant modifyDateStartWith) {
        this.modifyDateStartWith = modifyDateStartWith;
    }

    public Instant getModifyDateEndWith() {
        return modifyDateEndWith;
    }

    public void setModifyDateEndWith(Instant modifyDateEndWith) {
        this.modifyDateEndWith = modifyDateEndWith;
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
        private Instant createDateStartWith;
        private Instant createDateEndWith;
        private Instant modifyDateStartWith;
        private Instant modifyDateEndWith;

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

        public Builder createDateStartWith(Instant createDateStartWith) {
            this.createDateStartWith = createDateStartWith;
            return this;
        }

        public Builder createDateEndWith(Instant createDateEndWith) {
            this.createDateEndWith = createDateEndWith;
            return this;
        }

        public Builder modifyDateStartWith(Instant modifyDateStartWith) {
            this.modifyDateStartWith = modifyDateStartWith;
            return this;
        }

        public Builder modifyDateEndWith(Instant modifyDateEndWith) {
            this.modifyDateEndWith = modifyDateEndWith;
            return this;
        }

        public WorkshopFilter build() {
            return new WorkshopFilter(this);
        }
    }
}
