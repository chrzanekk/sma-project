package pl.com.chrzanowski.scma.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class ServiceActionDTO {

    private Long id;
    private Integer carMileage;
    private String invoiceNumber;
    private BigDecimal grossValue;
    private BigDecimal taxValue;
    private BigDecimal netValue;
    private BigDecimal taxRate;
    private LocalDate serviceDate;
    private String description;
    private Long workshopId;
    private String workshopName;
    private Set<ServiceActionTypeDTO> serviceActionTypes;
    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;
    private BigDecimal summaryGrossValue;
    private BigDecimal summaryTaxValue;
    private BigDecimal summaryNetValue;

    public ServiceActionDTO() {
    }

    public ServiceActionDTO(Long id,
                            Integer carMileage,
                            String invoiceNumber,
                            BigDecimal grossValue,
                            BigDecimal taxValue,
                            BigDecimal netValue,
                            BigDecimal taxRate,
                            LocalDate serviceDate,
                            String description,
                            Long workshopId,
                            String workshopName,
                            Set<ServiceActionTypeDTO> serviceActionTypes,
                            Long vehicleId,
                            String vehicleRegistrationNumber,
                            Instant createDate,
                            Instant modifyDate,
                            Instant removeDate,
                            BigDecimal summaryGrossValue,
                            BigDecimal summaryTaxValue,
                            BigDecimal summaryNetValue) {
        this.id = id;
        this.carMileage = carMileage;
        this.invoiceNumber = invoiceNumber;
        this.grossValue = grossValue;
        this.taxValue = taxValue;
        this.netValue = netValue;
        this.taxRate = taxRate;
        this.serviceDate = serviceDate;
        this.description = description;
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.serviceActionTypes = serviceActionTypes;
        this.vehicleId = vehicleId;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
        this.summaryGrossValue = summaryGrossValue;
        this.summaryTaxValue = summaryTaxValue;
        this.summaryNetValue = summaryNetValue;
    }

    private ServiceActionDTO(Builder builder) {
        id = builder.id;
        carMileage = builder.carMileage;
        invoiceNumber = builder.invoiceNumber;
        grossValue = builder.grossValue;
        taxValue = builder.taxValue;
        netValue = builder.netValue;
        taxRate = builder.taxRate;
        serviceDate = builder.serviceDate;
        description = builder.description;
        workshopId = builder.workshopId;
        workshopName = builder.workshopName;
        serviceActionTypes = builder.serviceActionTypes;
        vehicleId = builder.vehicleId;
        vehicleRegistrationNumber = builder.vehicleRegistrationNumber;
        createDate = builder.createDate;
        modifyDate = builder.modifyDate;
        removeDate = builder.removeDate;
        summaryGrossValue = builder.summaryGrossValue;
        summaryTaxValue = builder.summaryTaxValue;
        summaryNetValue = builder.summaryNetValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ServiceActionDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.carMileage = copy.getCarMileage();
        builder.invoiceNumber = copy.getInvoiceNumber();
        builder.grossValue = copy.getGrossValue();
        builder.taxValue = copy.getTaxValue();
        builder.netValue = copy.getNetValue();
        builder.taxRate = copy.getTaxRate();
        builder.serviceDate = copy.getServiceDate();
        builder.description = copy.getDescription();
        builder.workshopId = copy.getWorkshopId();
        builder.workshopName = copy.getWorkshopName();
        builder.serviceActionTypes = copy.getServiceActionTypes();
        builder.vehicleId = copy.getVehicleId();
        builder.vehicleRegistrationNumber = copy.getVehicleRegistrationNumber();
        builder.createDate = copy.getCreateDate();
        builder.modifyDate = copy.getModifyDate();
        builder.removeDate = copy.getRemoveDate();
        builder.summaryGrossValue = copy.getSummaryGrossValue();
        builder.summaryTaxValue = copy.getSummaryTaxValue();
        builder.summaryNetValue = copy.getSummaryNetValue();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public Integer getCarMileage() {
        return carMileage;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public BigDecimal getGrossValue() {
        return grossValue;
    }

    public BigDecimal getTaxValue() {
        return taxValue;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public String getDescription() {
        return description;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public Set<ServiceActionTypeDTO> getServiceActionTypes() {
        return serviceActionTypes;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
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

    public BigDecimal getSummaryGrossValue() {
        return summaryGrossValue;
    }

    public BigDecimal getSummaryTaxValue() {
        return summaryTaxValue;
    }

    public BigDecimal getSummaryNetValue() {
        return summaryNetValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceActionDTO that = (ServiceActionDTO) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(carMileage, that.carMileage)) return false;
        if (!Objects.equals(invoiceNumber, that.invoiceNumber))
            return false;
        if (!Objects.equals(grossValue, that.grossValue)) return false;
        if (!Objects.equals(taxValue, that.taxValue)) return false;
        if (!Objects.equals(netValue, that.netValue)) return false;
        if (!Objects.equals(taxRate, that.taxRate)) return false;
        if (!Objects.equals(serviceDate, that.serviceDate)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(workshopId, that.workshopId)) return false;
        if (!Objects.equals(workshopName, that.workshopName)) return false;
        if (!Objects.equals(serviceActionTypes, that.serviceActionTypes))
            return false;
        if (!Objects.equals(vehicleId, that.vehicleId)) return false;
        if (!Objects.equals(vehicleRegistrationNumber, that.vehicleRegistrationNumber))
            return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        if (!Objects.equals(modifyDate, that.modifyDate)) return false;
        if (!Objects.equals(removeDate, that.removeDate)) return false;
        if (!Objects.equals(summaryGrossValue, that.summaryGrossValue))
            return false;
        if (!Objects.equals(summaryTaxValue, that.summaryTaxValue))
            return false;
        return Objects.equals(summaryNetValue, that.summaryNetValue);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (carMileage != null ? carMileage.hashCode() : 0);
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        result = 31 * result + (grossValue != null ? grossValue.hashCode() : 0);
        result = 31 * result + (taxValue != null ? taxValue.hashCode() : 0);
        result = 31 * result + (netValue != null ? netValue.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        result = 31 * result + (serviceDate != null ? serviceDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (workshopId != null ? workshopId.hashCode() : 0);
        result = 31 * result + (workshopName != null ? workshopName.hashCode() : 0);
        result = 31 * result + (serviceActionTypes != null ? serviceActionTypes.hashCode() : 0);
        result = 31 * result + (vehicleId != null ? vehicleId.hashCode() : 0);
        result = 31 * result + (vehicleRegistrationNumber != null ? vehicleRegistrationNumber.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        result = 31 * result + (summaryGrossValue != null ? summaryGrossValue.hashCode() : 0);
        result = 31 * result + (summaryTaxValue != null ? summaryTaxValue.hashCode() : 0);
        result = 31 * result + (summaryNetValue != null ? summaryNetValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceActionDTO{" +
                "id=" + id +
                ", carMileage=" + carMileage +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", grossValue=" + grossValue +
                ", taxValue=" + taxValue +
                ", netValue=" + netValue +
                ", taxRate=" + taxRate +
                ", serviceDate=" + serviceDate +
                ", description='" + description + '\'' +
                ", workshopId=" + workshopId +
                ", workshopName='" + workshopName + '\'' +
                ", serviceActionTypes=" + serviceActionTypes +
                ", vehicleId=" + vehicleId +
                ", vehicleRegistrationNumber='" + vehicleRegistrationNumber + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                ", summaryGrossValue=" + summaryGrossValue +
                ", summaryTaxValue=" + summaryTaxValue +
                ", summaryNetValue=" + summaryNetValue +
                '}';
    }


    public static class Builder {
        private Long id;
        private Integer carMileage;
        private String invoiceNumber;
        private BigDecimal grossValue;
        private BigDecimal taxValue;
        private BigDecimal netValue;
        private BigDecimal taxRate;
        private LocalDate serviceDate;
        private String description;
        private Long workshopId;
        private String workshopName;
        private Set<ServiceActionTypeDTO> serviceActionTypes;
        private Long vehicleId;
        private String vehicleRegistrationNumber;
        private Instant createDate;
        private Instant modifyDate;
        private Instant removeDate;
        private BigDecimal summaryGrossValue;
        private BigDecimal summaryTaxValue;
        private BigDecimal summaryNetValue;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder carMileage(Integer carMileage) {
            this.carMileage = carMileage;
            return this;
        }

        public Builder invoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public Builder grossValue(BigDecimal grossValue) {
            this.grossValue = grossValue;
            return this;
        }

        public Builder taxValue(BigDecimal taxValue) {
            this.taxValue = taxValue;
            return this;
        }

        public Builder netValue(BigDecimal netValue) {
            this.netValue = netValue;
            return this;
        }

        public Builder taxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public Builder serviceDate(LocalDate serviceDate) {
            this.serviceDate = serviceDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder workshopId(Long workshopId) {
            this.workshopId = workshopId;
            return this;
        }

        public Builder workshopName(String workshopName) {
            this.workshopName = workshopName;
            return this;
        }

        public Builder serviceActionTypes(Set<ServiceActionTypeDTO> serviceActionTypes) {
            this.serviceActionTypes = serviceActionTypes;
            return this;
        }

        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
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

        public Builder summaryGrossValue(BigDecimal summaryGrossValue) {
            this.summaryGrossValue = summaryGrossValue;
            return this;
        }

        public Builder summaryTaxValue(BigDecimal summaryTaxValue) {
            this.summaryTaxValue = summaryTaxValue;
            return this;
        }

        public Builder summaryNetValue(BigDecimal summaryNetValue) {
            this.summaryNetValue = summaryNetValue;
            return this;
        }

        public ServiceActionDTO build() {
            return new ServiceActionDTO(this);
        }
    }
}
