package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "vehicle_model", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class VehicleModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200)
    @NotNull
    @NotBlank
    private String name;

    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;

    @ManyToOne
    @JoinColumn(name = "vehicle_brand")
    @NotNull
    private VehicleBrand vehicleBrand;

    public VehicleModel(Long id,
                        String name,
                        Instant createDate,
                        Instant modifyDate,
                        Instant removeDate,
                        VehicleBrand vehicleBrand) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
        this.vehicleBrand = vehicleBrand;
    }

    public VehicleModel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

     public VehicleBrand getVehicleBrand() {
        return vehicleBrand;
    }

    public VehicleModel setId(Long id) {
        this.id = id;
        return this;
    }

    public VehicleModel setName(String name) {
        this.name = name;
        return this;
    }

    public VehicleModel setCreateDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public VehicleModel setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public VehicleModel setRemoveDate(Instant removeDate) {
        this.removeDate = removeDate;
        return this;
    }

    public VehicleModel setVehicleBrand(VehicleBrand vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleModel that = (VehicleModel) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(createDate, that.createDate) && Objects.equals(modifyDate, that.modifyDate) && Objects.equals(removeDate, that.removeDate) && Objects.equals(vehicleBrand, that.vehicleBrand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createDate, modifyDate, removeDate, vehicleBrand);
    }

    @Override
    public String toString() {
        return "VehicleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                ", vehicleBrand=" + vehicleBrand +
                '}';
    }
}
