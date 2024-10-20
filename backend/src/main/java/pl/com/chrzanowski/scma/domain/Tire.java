package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.com.chrzanowski.scma.domain.enumeration.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tire")
public class Tire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand")
    @NotNull
    @NotBlank
    private String brand;

    @Column(name = "model")
    @NotNull
    @NotBlank
    private String model;

    @Column(name = "width")
    @NotNull
    private Integer width;

    @Column(name = "profile")
    @NotNull
    private Integer profile;

    @Column(name = "diameter")
    @NotNull
    private Integer diameter;
    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TireType type;

    @Column(name = "reinforced_index")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TireReinforcedIndex tireReinforcedIndex;
    @Column(name = "speed_index")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TireSpeedIndex speedIndex;

    @Column(name = "capacity_index")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TireLoadCapacityIndex capacityIndex;
    @Column(name = "season_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TireSeasonType tireSeasonType;

    @Column(name = "run_on_flat")
    @NotNull
    private Boolean runOnFlat;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TireStatus tireStatus;

    @Column(name = "production_year")
    @NotNull
    private Integer productionYear;


    @Column(name = "purchase_date")
    @NotNull
    private LocalDate purchaseDate;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;


    public Tire(Long id,
                String brand,
                String model,
                Integer width,
                Integer profile,
                Integer diameter,
                TireType type,
                TireReinforcedIndex tireReinforcedIndex,
                TireSpeedIndex speedIndex,
                TireLoadCapacityIndex capacityIndex,
                TireSeasonType tireSeasonType,
                Boolean runOnFlat,
                TireStatus tireStatus,
                Integer productionYear,
                LocalDate purchaseDate,
                Instant createDate,
                Instant modifyDate,
                Instant removeDate) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.width = width;
        this.profile = profile;
        this.diameter = diameter;
        this.type = type;
        this.tireReinforcedIndex = tireReinforcedIndex;
        this.speedIndex = speedIndex;
        this.capacityIndex = capacityIndex;
        this.tireSeasonType = tireSeasonType;
        this.runOnFlat = runOnFlat;
        this.tireStatus = tireStatus;
        this.productionYear = productionYear;
        this.purchaseDate = purchaseDate;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
    }

    public Tire() {
    }

    public Long getId() {
        return id;
    }

    public Tire setId(Long id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public Tire setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public Tire setModel(String model) {
        this.model = model;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public Tire setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getProfile() {
        return profile;
    }

    public Tire setProfile(Integer profile) {
        this.profile = profile;
        return this;
    }

    public Integer getDiameter() {
        return diameter;
    }

    public Tire setDiameter(Integer diameter) {
        this.diameter = diameter;
        return this;
    }

    public TireType getType() {
        return type;
    }

    public Tire setType(TireType type) {
        this.type = type;
        return this;
    }

    public TireReinforcedIndex getTireReinforcedIndex() {
        return tireReinforcedIndex;
    }

    public Tire setTireReinforcedIndex(TireReinforcedIndex tireReinforcedIndex) {
        this.tireReinforcedIndex = tireReinforcedIndex;
        return this;
    }

    public TireSpeedIndex getSpeedIndex() {
        return speedIndex;
    }

    public Tire setSpeedIndex(TireSpeedIndex speedIndex) {
        this.speedIndex = speedIndex;
        return this;
    }

    public TireLoadCapacityIndex getCapacityIndex() {
        return capacityIndex;
    }

    public Tire setCapacityIndex(TireLoadCapacityIndex capacityIndex) {
        this.capacityIndex = capacityIndex;
        return this;
    }

    public TireSeasonType getTireSeasonType() {
        return tireSeasonType;
    }

    public Tire setTireSeasonType(TireSeasonType tireSeasonType) {
        this.tireSeasonType = tireSeasonType;
        return this;
    }

    public Boolean getRunOnFlat() {
        return runOnFlat;
    }

    public Tire setRunOnFlat(Boolean runOnFlat) {
        this.runOnFlat = runOnFlat;
        return this;
    }

    public TireStatus getTireStatus() {
        return tireStatus;
    }

    public Tire setTireStatus(TireStatus tireStatus) {
        this.tireStatus = tireStatus;
        return this;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public Tire setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
        return this;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Tire setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Tire setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Tire setCreateDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public Tire setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public Instant getRemoveDate() {
        return removeDate;
    }

    public Tire setRemoveDate(Instant removeDate) {
        this.removeDate = removeDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tire tire = (Tire) o;

        if (!Objects.equals(id, tire.id)) return false;
        if (!Objects.equals(brand, tire.brand)) return false;
        if (!Objects.equals(model, tire.model)) return false;
        if (!Objects.equals(width, tire.width)) return false;
        if (!Objects.equals(profile, tire.profile)) return false;
        if (!Objects.equals(diameter, tire.diameter)) return false;
        if (type != tire.type) return false;
        if (tireReinforcedIndex != tire.tireReinforcedIndex) return false;
        if (speedIndex != tire.speedIndex) return false;
        if (capacityIndex != tire.capacityIndex) return false;
        if (tireSeasonType != tire.tireSeasonType) return false;
        if (!Objects.equals(runOnFlat, tire.runOnFlat)) return false;
        if (tireStatus != tire.tireStatus) return false;
        if (!Objects.equals(productionYear, tire.productionYear))
            return false;
        if (!Objects.equals(purchaseDate, tire.purchaseDate)) return false;
        if (!Objects.equals(vehicle, tire.vehicle)) return false;
        if (!Objects.equals(createDate, tire.createDate)) return false;
        if (!Objects.equals(modifyDate, tire.modifyDate)) return false;
        return Objects.equals(removeDate, tire.removeDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (profile != null ? profile.hashCode() : 0);
        result = 31 * result + (diameter != null ? diameter.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (tireReinforcedIndex != null ? tireReinforcedIndex.hashCode() : 0);
        result = 31 * result + (speedIndex != null ? speedIndex.hashCode() : 0);
        result = 31 * result + (capacityIndex != null ? capacityIndex.hashCode() : 0);
        result = 31 * result + (tireSeasonType != null ? tireSeasonType.hashCode() : 0);
        result = 31 * result + (runOnFlat != null ? runOnFlat.hashCode() : 0);
        result = 31 * result + (tireStatus != null ? tireStatus.hashCode() : 0);
        result = 31 * result + (productionYear != null ? productionYear.hashCode() : 0);
        result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
        result = 31 * result + (vehicle != null ? vehicle.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", width=" + width +
                ", profile=" + profile +
                ", diameter=" + diameter +
                ", type=" + type +
                ", tireReinforcedIndex=" + tireReinforcedIndex +
                ", speedIndex=" + speedIndex +
                ", capacityIndex=" + capacityIndex +
                ", tireSeasonType=" + tireSeasonType +
                ", runOnFlat=" + runOnFlat +
                ", tireStatus=" + tireStatus +
                ", productionYear=" + productionYear +
                ", purchaseDate=" + purchaseDate +
                ", vehicle=" + vehicle +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                '}';
    }
}
