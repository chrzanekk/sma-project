package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "vin")
    private String vin;

    @Column(name = "production_year")
    private Short productionYear;

    @Column(name = "first_registration_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate firstRegistrationDate;

    @Column(name = "free_places_for_tech_inspection")
    private Short freePlacesForTechInspection;

    @Column(name = "length")
    private BigDecimal length;

    @Column(name = "width")
    private BigDecimal width;

    @Column(name = "height")
    private BigDecimal height;

    @Column(name = "create_date")
    private Instant createDate;
    @Column(name = "modify_date")
    private Instant modifyDate;

    @ManyToOne
    @JoinColumn(name = "vehicle_model")
    @NotNull
    private VehicleModel vehicleModel;

    @ManyToOne
    @JoinColumn(name = "vehicle_type")
    @NotNull
    private VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "fuel_type")
    @NotNull
    private FuelType fuelType;

    @OneToMany
    List<ServiceAction> serviceActions = new ArrayList<>();

    public Vehicle(Long id,
                   String registrationNumber,
                   String vin,
                   Short productionYear,
                   LocalDate firstRegistrationDate,
                   Short freePlacesForTechInspection,
                   BigDecimal length,
                   BigDecimal width,
                   BigDecimal height,
                   Instant createDate,
                   Instant modifyDate,
                   VehicleModel vehicleModel,
                   VehicleType vehicleType,
                   FuelType fuelType,
                   List<ServiceAction> serviceActions) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.vin = vin;
        this.productionYear = productionYear;
        this.firstRegistrationDate = firstRegistrationDate;
        this.freePlacesForTechInspection = freePlacesForTechInspection;
        this.length = length;
        this.width = width;
        this.height = height;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.serviceActions = serviceActions;
    }

    public Vehicle() {
    }

    public Long getId() {
        return id;
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

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public List<ServiceAction> getServiceActions() {
        return serviceActions;
    }

    public Vehicle setId(Long id) {
        this.id = id;
        return this;
    }

    public Vehicle setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        return this;
    }

    public Vehicle setVin(String vin) {
        this.vin = vin;
        return this;
    }

    public Vehicle setProductionYear(Short productionYear) {
        this.productionYear = productionYear;
        return this;
    }

    public Vehicle setFirstRegistrationDate(LocalDate firstRegistrationDate) {
        this.firstRegistrationDate = firstRegistrationDate;
        return this;
    }

    public Vehicle setFreePlacesForTechInspection(Short freePlacesForTechInspection) {
        this.freePlacesForTechInspection = freePlacesForTechInspection;
        return this;
    }

    public Vehicle setLength(BigDecimal length) {
        this.length = length;
        return this;
    }

    public Vehicle setWidth(BigDecimal width) {
        this.width = width;
        return this;
    }

    public Vehicle setHeight(BigDecimal height) {
        this.height = height;
        return this;
    }

    public Vehicle setCreateDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public Vehicle setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }


    public Vehicle setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
        return this;
    }

    public Vehicle setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public Vehicle setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
        return this;
    }

    public Vehicle setServiceActions(List<ServiceAction> serviceActions) {
        this.serviceActions = serviceActions;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        if (!Objects.equals(id, vehicle.id)) return false;
        if (!Objects.equals(registrationNumber, vehicle.registrationNumber))
            return false;
        if (!Objects.equals(vin, vehicle.vin)) return false;
        if (!Objects.equals(productionYear, vehicle.productionYear))
            return false;
        if (!Objects.equals(firstRegistrationDate, vehicle.firstRegistrationDate))
            return false;
        if (!Objects.equals(freePlacesForTechInspection, vehicle.freePlacesForTechInspection))
            return false;
        if (!Objects.equals(length, vehicle.length)) return false;
        if (!Objects.equals(width, vehicle.width)) return false;
        if (!Objects.equals(height, vehicle.height)) return false;
        if (!Objects.equals(createDate, vehicle.createDate)) return false;
        if (!Objects.equals(modifyDate, vehicle.modifyDate)) return false;
        if (!Objects.equals(vehicleModel, vehicle.vehicleModel))
            return false;
        if (!Objects.equals(vehicleType, vehicle.vehicleType)) return false;
        if (!Objects.equals(fuelType, vehicle.fuelType)) return false;
        return Objects.equals(serviceActions, vehicle.serviceActions);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (vin != null ? vin.hashCode() : 0);
        result = 31 * result + (productionYear != null ? productionYear.hashCode() : 0);
        result = 31 * result + (firstRegistrationDate != null ? firstRegistrationDate.hashCode() : 0);
        result = 31 * result + (freePlacesForTechInspection != null ? freePlacesForTechInspection.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (vehicleModel != null ? vehicleModel.hashCode() : 0);
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (fuelType != null ? fuelType.hashCode() : 0);
        result = 31 * result + (serviceActions != null ? serviceActions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", vin='" + vin + '\'' +
                ", productionYear=" + productionYear +
                ", firstRegistrationDate=" + firstRegistrationDate +
                ", freePlacesForTechInspection=" + freePlacesForTechInspection +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", vehicleModel=" + vehicleModel +
                ", vehicleType=" + vehicleType +
                ", fuelType=" + fuelType +
                ", serviceActions=" + serviceActions +
                '}';
    }
}
