package pl.com.chrzanowski.scma.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class VehicleDTO {

    private Long id;
    private Long brandId;
    private String brandName;
    private Long modelId;
    private String modelName;
    private String registrationNumber;
    private String vin;
    private Short productionYear;
    private LocalDate firstRegistrationDate;
    private Short freePlacesForTechInspection;
    private Long fuelTypeId;
    private String fuelTypeName;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private Instant createDate;
    private Instant modifyDate;

    public VehicleDTO(Long id,
                      Long brandId,
                      String brandName,
                      Long modelId,
                      String modelName,
                      String registrationNumber,
                      String vin,
                      Short productionYear,
                      LocalDate firstRegistrationDate,
                      Short freePlacesForTechInspection,
                      Long fuelTypeId,
                      String fuelTypeName,
                      Long vehicleTypeId,
                      String vehicleTypeName,
                      BigDecimal length,
                      BigDecimal width,
                      BigDecimal height,
                      Instant createDate,
                      Instant modifyDate) {
        this.id = id;
        this.brandId = brandId;
        this.brandName = brandName;
        this.modelId = modelId;
        this.modelName = modelName;
        this.registrationNumber = registrationNumber;
        this.vin = vin;
        this.productionYear = productionYear;
        this.firstRegistrationDate = firstRegistrationDate;
        this.freePlacesForTechInspection = freePlacesForTechInspection;
        this.fuelTypeId = fuelTypeId;
        this.fuelTypeName = fuelTypeName;
        this.vehicleTypeId = vehicleTypeId;
        this.vehicleTypeName = vehicleTypeName;
        this.length = length;
        this.width = width;
        this.height = height;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public VehicleDTO() {
    }

    private VehicleDTO(Builder builder) {
        id = builder.id;
        brandId = builder.brandId;
        brandName = builder.brandName;
        modelId = builder.modelId;
        modelName = builder.modelName;
        registrationNumber = builder.registrationNumber;
        vin = builder.vin;
        productionYear = builder.productionYear;
        firstRegistrationDate = builder.firstRegistrationDate;
        freePlacesForTechInspection = builder.freePlacesForTechInspection;
        fuelTypeId = builder.fuelTypeId;
        fuelTypeName = builder.fuelTypeName;
        vehicleTypeId = builder.vehicleTypeId;
        vehicleTypeName = builder.vehicleTypeName;
        length = builder.length;
        width = builder.width;
        height = builder.height;
        createDate = builder.createDate;
        modifyDate = builder.modifyDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(VehicleDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.brandId = copy.getBrandId();
        builder.brandName = copy.getBrandName();
        builder.modelId = copy.getModelId();
        builder.modelName = copy.getModelName();
        builder.registrationNumber = copy.getRegistrationNumber();
        builder.vin = copy.getVin();
        builder.productionYear = copy.getProductionYear();
        builder.firstRegistrationDate = copy.getFirstRegistrationDate();
        builder.freePlacesForTechInspection = copy.getFreePlacesForTechInspection();
        builder.fuelTypeId = copy.getFuelTypeId();
        builder.fuelTypeName = copy.getFuelTypeName();
        builder.vehicleTypeId = copy.getVehicleTypeId();
        builder.vehicleTypeName = copy.getVehicleTypeName();
        builder.length = copy.getLength();
        builder.width = copy.getWidth();
        builder.height = copy.getHeight();
        builder.createDate = copy.getCreateDate();
        builder.modifyDate = copy.getModifyDate();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public Long getModelId() {
        return modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVin() {
        return vin;
    }

    public Short getProductionYear() {
        return productionYear;
    }

    public LocalDate getFirstRegistrationDate() {
        return firstRegistrationDate;
    }

    public Short getFreePlacesForTechInspection() {
        return freePlacesForTechInspection;
    }

    public Long getFuelTypeId() {
        return fuelTypeId;
    }

    public String getFuelTypeName() {
        return fuelTypeName;
    }

    public Long getVehicleTypeId() {
        return vehicleTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VehicleDTO that = (VehicleDTO) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(brandId, that.brandId)) return false;
        if (!Objects.equals(brandName, that.brandName)) return false;
        if (!Objects.equals(modelId, that.modelId)) return false;
        if (!Objects.equals(modelName, that.modelName)) return false;
        if (!Objects.equals(registrationNumber, that.registrationNumber))
            return false;
        if (!Objects.equals(vin, that.vin)) return false;
        if (!Objects.equals(productionYear, that.productionYear))
            return false;
        if (!Objects.equals(firstRegistrationDate, that.firstRegistrationDate))
            return false;
        if (!Objects.equals(freePlacesForTechInspection, that.freePlacesForTechInspection))
            return false;
        if (!Objects.equals(fuelTypeId, that.fuelTypeId)) return false;
        if (!Objects.equals(fuelTypeName, that.fuelTypeName)) return false;
        if (!Objects.equals(vehicleTypeId, that.vehicleTypeId))
            return false;
        if (!Objects.equals(vehicleTypeName, that.vehicleTypeName))
            return false;
        if (!Objects.equals(length, that.length)) return false;
        if (!Objects.equals(width, that.width)) return false;
        if (!Objects.equals(height, that.height)) return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        return Objects.equals(modifyDate, that.modifyDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (brandId != null ? brandId.hashCode() : 0);
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + (modelId != null ? modelId.hashCode() : 0);
        result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (vin != null ? vin.hashCode() : 0);
        result = 31 * result + (productionYear != null ? productionYear.hashCode() : 0);
        result = 31 * result + (firstRegistrationDate != null ? firstRegistrationDate.hashCode() : 0);
        result = 31 * result + (freePlacesForTechInspection != null ? freePlacesForTechInspection.hashCode() : 0);
        result = 31 * result + (fuelTypeId != null ? fuelTypeId.hashCode() : 0);
        result = 31 * result + (fuelTypeName != null ? fuelTypeName.hashCode() : 0);
        result = 31 * result + (vehicleTypeId != null ? vehicleTypeId.hashCode() : 0);
        result = 31 * result + (vehicleTypeName != null ? vehicleTypeName.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", vin='" + vin + '\'' +
                ", productionYear=" + productionYear +
                ", firstRegistrationDate=" + firstRegistrationDate +
                ", freePlacesForTechInspection=" + freePlacesForTechInspection +
                ", fuelTypeId=" + fuelTypeId +
                ", fuelTypeName='" + fuelTypeName + '\'' +
                ", vehicleTypeId=" + vehicleTypeId +
                ", vehicleTypeName='" + vehicleTypeName + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                '}';
    }


    public static final class Builder {
        private Long id;
        private Long brandId;
        private String brandName;
        private Long modelId;
        private String modelName;
        private String registrationNumber;
        private String vin;
        private Short productionYear;

        private LocalDate firstRegistrationDate;
        private Short freePlacesForTechInspection;
        private Long fuelTypeId;
        private String fuelTypeName;
        private Long vehicleTypeId;
        private String vehicleTypeName;
        private BigDecimal length;
        private BigDecimal width;
        private BigDecimal height;
        private Instant createDate;
        private Instant modifyDate;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder brandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public Builder modelId(Long modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder vin(String vin) {
            this.vin = vin;
            return this;
        }

        public Builder productionYear(Short productionYear) {
            this.productionYear = productionYear;
            return this;
        }

        public Builder firstRegistrationDate(LocalDate firstRegistrationDate) {
            this.firstRegistrationDate = firstRegistrationDate;
            return this;
        }

        public Builder freePlacesForTechInspection(Short freePlacesForTechInspection) {
            this.freePlacesForTechInspection = freePlacesForTechInspection;
            return this;
        }

        public Builder fuelTypeId(Long fuelTypeId) {
            this.fuelTypeId = fuelTypeId;
            return this;
        }

        public Builder fuelTypeName(String fuelTypeName) {
            this.fuelTypeName = fuelTypeName;
            return this;
        }

        public Builder vehicleTypeId(Long vehicleTypeId) {
            this.vehicleTypeId = vehicleTypeId;
            return this;
        }

        public Builder vehicleTypeName(String vehicleTypeName) {
            this.vehicleTypeName = vehicleTypeName;
            return this;
        }

        public Builder length(BigDecimal length) {
            this.length = length;
            return this;
        }

        public Builder width(BigDecimal width) {
            this.width = width;
            return this;
        }

        public Builder height(BigDecimal height) {
            this.height = height;
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

        public VehicleDTO build() {
            return new VehicleDTO(this);
        }
    }
}
