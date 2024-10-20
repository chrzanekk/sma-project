package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;


@Entity
@Table(name = "fuel_type")
public class FuelType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @NotNull
    @NotBlank
    private String name;
    @Column(name = "create_date", columnDefinition = "DATETIME")
    private Instant createDate;
    @Column(name = "modify_date", columnDefinition = "DATETIME")
    private Instant modifyDate;
    @Column(name = "remove_date", columnDefinition = "DATETIME")
    private Instant removeDate;

    public FuelType() {
    }

    public FuelType(Long id, String name, Instant createDate, Instant modifyDate, Instant removeDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Instant getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Instant removeDate) {
        this.removeDate = removeDate;
    }

    public FuelType name(String name) {
        this.name = name;
        return this;
    }

    public FuelType createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public FuelType modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public FuelType removeDate(Instant removeDate) {
        this.removeDate = removeDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelType fuelType = (FuelType) o;
        return Objects.equals(id, fuelType.id) && Objects.equals(name, fuelType.name) && Objects.equals(createDate, fuelType.createDate) && Objects.equals(modifyDate, fuelType.modifyDate) && Objects.equals(removeDate, fuelType.removeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createDate, modifyDate, removeDate);
    }

    @Override
    public String toString() {
        return "FuelType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                '}';
    }
}
