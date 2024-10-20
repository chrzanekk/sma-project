package pl.com.chrzanowski.scma.service.filter.serviceaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class ServiceActionFilter {

    private Long id;
    private Integer carMileageStartsWith;
    private Integer carMileageEndWith;
    private String invoiceNumber;
    private BigDecimal grossValueStartsWith;
    private BigDecimal grossValueEndWith;
    private BigDecimal taxValueStartsWith;
    private BigDecimal taxValueEndWith;
    private BigDecimal netValueStartsWith;
    private BigDecimal netValueEndWith;
    private BigDecimal taxRateStartsWith;
    private BigDecimal taxRateEndWith;
    private LocalDate serviceDateStartsWith;
    private LocalDate serviceDateEndWith;
    private String description;
    private Long workshopId;
    private String workshopName;
    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;

    public ServiceActionFilter(Long id,
                               Integer carMileageStartsWith,
                               Integer carMileageEndWith,
                               String invoiceNumber,
                               BigDecimal grossValueStartsWith,
                               BigDecimal grossValueEndWith,
                               BigDecimal taxValueStartsWith,
                               BigDecimal taxValueEndWith,
                               BigDecimal netValueStartsWith,
                               BigDecimal netValueEndWith,
                               BigDecimal taxRateStartsWith,
                               BigDecimal taxRateEndWith,
                               LocalDate serviceDateStartsWith,
                               LocalDate serviceDateEndWith,
                               String description,
                               Long workshopId,
                               String workshopName,
                               Long vehicleId,
                               String vehicleRegistrationNumber,
                               Instant createDateStartWith,
                               Instant createDateEndWith,
                               Instant modifyDateStartWith,
                               Instant modifyDateEndWith) {
        this.id = id;
        this.carMileageStartsWith = carMileageStartsWith;
        this.carMileageEndWith = carMileageEndWith;
        this.invoiceNumber = invoiceNumber;
        this.grossValueStartsWith = grossValueStartsWith;
        this.grossValueEndWith = grossValueEndWith;
        this.taxValueStartsWith = taxValueStartsWith;
        this.taxValueEndWith = taxValueEndWith;
        this.netValueStartsWith = netValueStartsWith;
        this.netValueEndWith = netValueEndWith;
        this.taxRateStartsWith = taxRateStartsWith;
        this.taxRateEndWith = taxRateEndWith;
        this.serviceDateStartsWith = serviceDateStartsWith;
        this.serviceDateEndWith = serviceDateEndWith;
        this.description = description;
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.vehicleId = vehicleId;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.createDateStartWith = createDateStartWith;
        this.createDateEndWith = createDateEndWith;
        this.modifyDateStartWith = modifyDateStartWith;
        this.modifyDateEndWith = modifyDateEndWith;
    }

    private ServiceActionFilter(Builder builder) {
        setId(builder.id);
        setCarMileageStartsWith(builder.carMileageStartsWith);
        setCarMileageEndWith(builder.carMileageEndWith);
        setInvoiceNumber(builder.invoiceNumber);
        setGrossValueStartsWith(builder.grossValueStartsWith);
        setGrossValueEndWith(builder.grossValueEndWith);
        setTaxValueStartsWith(builder.taxValueStartsWith);
        setTaxValueEndWith(builder.taxValueEndWith);
        setNetValueStartsWith(builder.netValueStartsWith);
        setNetValueEndWith(builder.netValueEndWith);
        setTaxRateStartsWith(builder.taxRateStartsWith);
        setTaxRateEndWith(builder.taxRateEndWith);
        setServiceDateStartsWith(builder.serviceDateStartsWith);
        setServiceDateEndWith(builder.serviceDateEndWith);
        setDescription(builder.description);
        setWorkshopId(builder.workshopId);
        setWorkshopName(builder.workshopName);
        setVehicleId(builder.vehicleId);
        setVehicleRegistrationNumber(builder.vehicleRegistrationNumber);
        setCreateDateStartWith(builder.createDateStartWith);
        setCreateDateEndWith(builder.createDateEndWith);
        setModifyDateStartWith(builder.modifyDateStartWith);
        setModifyDateEndWith(builder.modifyDateEndWith);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ServiceActionFilter copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.carMileageStartsWith = copy.getCarMileageStartsWith();
        builder.carMileageEndWith = copy.getCarMileageEndWith();
        builder.invoiceNumber = copy.getInvoiceNumber();
        builder.grossValueStartsWith = copy.getGrossValueStartsWith();
        builder.grossValueEndWith = copy.getGrossValueEndWith();
        builder.taxValueStartsWith = copy.getTaxValueStartsWith();
        builder.taxValueEndWith = copy.getTaxValueEndWith();
        builder.netValueStartsWith = copy.getNetValueStartsWith();
        builder.netValueEndWith = copy.getNetValueEndWith();
        builder.taxRateStartsWith = copy.getTaxRateStartsWith();
        builder.taxRateEndWith = copy.getTaxRateEndWith();
        builder.serviceDateStartsWith = copy.getServiceDateStartsWith();
        builder.serviceDateEndWith = copy.getServiceDateEndWith();
        builder.description = copy.getDescription();
        builder.workshopId = copy.getWorkshopId();
        builder.workshopName = copy.getWorkshopName();
        builder.vehicleId = copy.getVehicleId();
        builder.vehicleRegistrationNumber = copy.getVehicleRegistrationNumber();
        builder.createDateStartWith = copy.getCreateDateStartWith();
        builder.createDateEndWith = copy.getCreateDateEndWith();
        builder.modifyDateStartWith = copy.getModifyDateStartWith();
        builder.modifyDateEndWith = copy.getModifyDateEndWith();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public ServiceActionFilter setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCarMileageStartsWith() {
        return carMileageStartsWith;
    }

    public ServiceActionFilter setCarMileageStartsWith(Integer carMileageStartsWith) {
        this.carMileageStartsWith = carMileageStartsWith;
        return this;
    }

    public Integer getCarMileageEndWith() {
        return carMileageEndWith;
    }

    public ServiceActionFilter setCarMileageEndWith(Integer carMileageEndWith) {
        this.carMileageEndWith = carMileageEndWith;
        return this;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public ServiceActionFilter setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public BigDecimal getGrossValueStartsWith() {
        return grossValueStartsWith;
    }

    public ServiceActionFilter setGrossValueStartsWith(BigDecimal grossValueStartsWith) {
        this.grossValueStartsWith = grossValueStartsWith;
        return this;
    }

    public BigDecimal getGrossValueEndWith() {
        return grossValueEndWith;
    }

    public ServiceActionFilter setGrossValueEndWith(BigDecimal grossValueEndWith) {
        this.grossValueEndWith = grossValueEndWith;
        return this;
    }

    public BigDecimal getTaxValueStartsWith() {
        return taxValueStartsWith;
    }

    public ServiceActionFilter setTaxValueStartsWith(BigDecimal taxValueStartsWith) {
        this.taxValueStartsWith = taxValueStartsWith;
        return this;
    }

    public BigDecimal getTaxValueEndWith() {
        return taxValueEndWith;
    }

    public ServiceActionFilter setTaxValueEndWith(BigDecimal taxValueEndWith) {
        this.taxValueEndWith = taxValueEndWith;
        return this;
    }

    public BigDecimal getNetValueStartsWith() {
        return netValueStartsWith;
    }

    public ServiceActionFilter setNetValueStartsWith(BigDecimal netValueStartsWith) {
        this.netValueStartsWith = netValueStartsWith;
        return this;
    }

    public BigDecimal getNetValueEndWith() {
        return netValueEndWith;
    }

    public ServiceActionFilter setNetValueEndWith(BigDecimal netValueEndWith) {
        this.netValueEndWith = netValueEndWith;
        return this;
    }

    public BigDecimal getTaxRateStartsWith() {
        return taxRateStartsWith;
    }

    public ServiceActionFilter setTaxRateStartsWith(BigDecimal taxRateStartsWith) {
        this.taxRateStartsWith = taxRateStartsWith;
        return this;
    }

    public BigDecimal getTaxRateEndWith() {
        return taxRateEndWith;
    }

    public ServiceActionFilter setTaxRateEndWith(BigDecimal taxRateEndWith) {
        this.taxRateEndWith = taxRateEndWith;
        return this;
    }

    public LocalDate getServiceDateStartsWith() {
        return serviceDateStartsWith;
    }

    public ServiceActionFilter setServiceDateStartsWith(LocalDate serviceDateStartsWith) {
        this.serviceDateStartsWith = serviceDateStartsWith;
        return this;
    }

    public LocalDate getServiceDateEndWith() {
        return serviceDateEndWith;
    }

    public ServiceActionFilter setServiceDateEndWith(LocalDate serviceDateEndWith) {
        this.serviceDateEndWith = serviceDateEndWith;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceActionFilter setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public ServiceActionFilter setWorkshopId(Long workshopId) {
        this.workshopId = workshopId;
        return this;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public ServiceActionFilter setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
        return this;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public ServiceActionFilter setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public ServiceActionFilter setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        return this;
    }

    public Instant getCreateDateStartWith() {
        return createDateStartWith;
    }

    public ServiceActionFilter setCreateDateStartWith(Instant createDateStartWith) {
        this.createDateStartWith = createDateStartWith;
        return this;
    }

    public Instant getCreateDateEndWith() {
        return createDateEndWith;
    }

    public ServiceActionFilter setCreateDateEndWith(Instant createDateEndWith) {
        this.createDateEndWith = createDateEndWith;
        return this;
    }

    public Instant getModifyDateStartWith() {
        return modifyDateStartWith;
    }

    public ServiceActionFilter setModifyDateStartWith(Instant modifyDateStartWith) {
        this.modifyDateStartWith = modifyDateStartWith;
        return this;
    }

    public Instant getModifyDateEndWith() {
        return modifyDateEndWith;
    }

    public ServiceActionFilter setModifyDateEndWith(Instant modifyDateEndWith) {
        this.modifyDateEndWith = modifyDateEndWith;
        return this;
    }


    public static final class Builder {
        private Long id;
        private Integer carMileageStartsWith;
        private Integer carMileageEndWith;
        private String invoiceNumber;
        private BigDecimal grossValueStartsWith;
        private BigDecimal grossValueEndWith;
        private BigDecimal taxValueStartsWith;
        private BigDecimal taxValueEndWith;
        private BigDecimal netValueStartsWith;
        private BigDecimal netValueEndWith;
        private BigDecimal taxRateStartsWith;
        private BigDecimal taxRateEndWith;
        private LocalDate serviceDateStartsWith;
        private LocalDate serviceDateEndWith;
        private String description;
        private Long workshopId;
        private String workshopName;
        private Long vehicleId;
        private String vehicleRegistrationNumber;
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

        public Builder carMileageStartsWith(Integer carMileageStartsWith) {
            this.carMileageStartsWith = carMileageStartsWith;
            return this;
        }

        public Builder carMileageEndWith(Integer carMileageEndWith) {
            this.carMileageEndWith = carMileageEndWith;
            return this;
        }

        public Builder invoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public Builder grossValueStartsWith(BigDecimal grossValueStartsWith) {
            this.grossValueStartsWith = grossValueStartsWith;
            return this;
        }

        public Builder grossValueEndWith(BigDecimal grossValueEndWith) {
            this.grossValueEndWith = grossValueEndWith;
            return this;
        }

        public Builder taxValueStartsWith(BigDecimal taxValueStartsWith) {
            this.taxValueStartsWith = taxValueStartsWith;
            return this;
        }

        public Builder taxValueEndWith(BigDecimal taxValueEndWith) {
            this.taxValueEndWith = taxValueEndWith;
            return this;
        }

        public Builder netValueStartsWith(BigDecimal netValueStartsWith) {
            this.netValueStartsWith = netValueStartsWith;
            return this;
        }

        public Builder netValueEndWith(BigDecimal netValueEndWith) {
            this.netValueEndWith = netValueEndWith;
            return this;
        }

        public Builder taxRateStartsWith(BigDecimal taxRateStartsWith) {
            this.taxRateStartsWith = taxRateStartsWith;
            return this;
        }

        public Builder taxRateEndWith(BigDecimal taxRateEndWith) {
            this.taxRateEndWith = taxRateEndWith;
            return this;
        }

        public Builder serviceDateStartsWith(LocalDate serviceDateStartsWith) {
            this.serviceDateStartsWith = serviceDateStartsWith;
            return this;
        }

        public Builder serviceDateEndWith(LocalDate serviceDateEndWith) {
            this.serviceDateEndWith = serviceDateEndWith;
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

        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
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

        public ServiceActionFilter build() {
            return new ServiceActionFilter(this);
        }
    }
}
