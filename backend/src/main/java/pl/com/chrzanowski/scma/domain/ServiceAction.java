package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "service_action")
public class ServiceAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_mileage")
    @NotNull
    private Integer carMileage;
    @Column(name = "invoice_no")
    @NotNull
    private String invoiceNumber;
    @Column(name = "invoice_groos_value")
    private BigDecimal grossValue;
    @Column(name = "tax_value")
    private BigDecimal taxValue;
    @Column(name = "invoice_net_value")
    @NotNull
    private BigDecimal netValue;
    @Column(name = "tax_rate")
    @NotNull
    private BigDecimal taxRate;
    @Column(name = "service_date")
    @NotNull
    private LocalDate serviceDate;
    @Column(name = "description", length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "workshop_id")
    @NotNull
    private Workshop workshop;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "service_action_service_action_type",
            joinColumns = @JoinColumn(name = "service_action_id"),
            inverseJoinColumns = @JoinColumn(name = "service_action_type_id"))
    private Set<ServiceActionType> serviceActionTypes = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @NotNull
    private Vehicle vehicle;

    @Column(name = "create_date", columnDefinition = "DATETIME")
    private Instant createDate;
    @Column(name = "modify_date", columnDefinition = "DATETIME")
    private Instant modifyDate;
    @Column(name = "remove_date", columnDefinition = "DATETIME")
    private Instant removeDate;

    public ServiceAction(Long id,
                         Integer carMileage,
                         String invoiceNumber,
                         BigDecimal grossValue,
                         BigDecimal taxValue,
                         BigDecimal netValue,
                         BigDecimal taxRate,
                         LocalDate serviceDate,
                         String description,
                         Workshop workshop,
                         Set<ServiceActionType> serviceActionTypes,
                         Vehicle vehicle,
                         Instant createDate,
                         Instant modifyDate,
                         Instant removeDate) {
        this.id = id;
        this.carMileage = carMileage;
        this.invoiceNumber = invoiceNumber;
        this.grossValue = grossValue;
        this.taxValue = taxValue;
        this.netValue = netValue;
        this.taxRate = taxRate;
        this.serviceDate = serviceDate;
        this.description = description;
        this.workshop = workshop;
        this.serviceActionTypes = serviceActionTypes;
        this.vehicle = vehicle;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
    }

    public ServiceAction() {
    }

    public Long getId() {
        return id;
    }

    public ServiceAction setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCarMileage() {
        return carMileage;
    }

    public ServiceAction setCarMileage(Integer carMileage) {
        this.carMileage = carMileage;
        return this;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public ServiceAction setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public BigDecimal getGrossValue() {
        return grossValue;
    }

    public ServiceAction setGrossValue(BigDecimal grossValue) {
        this.grossValue = grossValue;
        return this;
    }

    public BigDecimal getTaxValue() {
        return taxValue;
    }

    public ServiceAction setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
        return this;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public ServiceAction setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
        return this;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public ServiceAction setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public ServiceAction setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceAction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public ServiceAction setWorkshop(Workshop workshop) {
        this.workshop = workshop;
        return this;
    }

    public Set<ServiceActionType> getServiceActionTypes() {
        return serviceActionTypes;
    }

    public ServiceAction setServiceActionTypes(Set<ServiceActionType> serviceActionTypes) {
        this.serviceActionTypes = serviceActionTypes;
        return this;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ServiceAction setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public ServiceAction setCreateDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public ServiceAction setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public Instant getRemoveDate() {
        return removeDate;
    }

    public ServiceAction setRemoveDate(Instant removeDate) {
        this.removeDate = removeDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceAction that = (ServiceAction) o;

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
        if (!Objects.equals(workshop, that.workshop)) return false;
        if (!Objects.equals(serviceActionTypes, that.serviceActionTypes))
            return false;
        if (!Objects.equals(vehicle, that.vehicle)) return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        if (!Objects.equals(modifyDate, that.modifyDate)) return false;
        return Objects.equals(removeDate, that.removeDate);
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
        result = 31 * result + (workshop != null ? workshop.hashCode() : 0);
        result = 31 * result + (serviceActionTypes != null ? serviceActionTypes.hashCode() : 0);
        result = 31 * result + (vehicle != null ? vehicle.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceAction{" +
                "id=" + id +
                ", carMileage=" + carMileage +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", grossValue=" + grossValue +
                ", taxValue=" + taxValue +
                ", netValue=" + netValue +
                ", taxRate=" + taxRate +
                ", serviceDate=" + serviceDate +
                ", description='" + description + '\'' +
                ", workshop=" + workshop +
                ", serviceActionTypes=" + serviceActionTypes +
                ", vehicle=" + vehicle +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                '}';
    }
}
