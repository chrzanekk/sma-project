package pl.com.chrzanowski.scma.service.filter.vehiclemodel;

import java.time.Instant;
import java.util.Objects;

public class VehicleModelFilter {

    private Long id;
    private String name;
    private Long vehicleBrandId;
    private String vehicleBrandName;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;

    public VehicleModelFilter() {
    }

    private VehicleModelFilter(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setVehicleBrandId(builder.vehicleBrandId);
        setVehicleBrandName(builder.vehicleBrandName);
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

    public String getName() {
        return name;
    }

    public Instant getCreateDateStartWith() {
        return createDateStartWith;
    }

    public Instant getCreateDateEndWith() {
        return createDateEndWith;
    }

    public Instant getModifyDateStartWith() {
        return modifyDateStartWith;
    }

    public Instant getModifyDateEndWith() {
        return modifyDateEndWith;
    }

    public Long getVehicleBrandId() {
        return vehicleBrandId;
    }

    public String getVehicleBrandName() {
        return vehicleBrandName;
    }

    public VehicleModelFilter setId(Long id) {
        this.id = id;
        return this;
    }

    public VehicleModelFilter setName(String name) {
        this.name = name;
        return this;
    }

    public VehicleModelFilter setCreateDateStartWith(Instant createDateStartWith) {
        this.createDateStartWith = createDateStartWith;
        return this;
    }

    public VehicleModelFilter setCreateDateEndWith(Instant createDateEndWith) {
        this.createDateEndWith = createDateEndWith;
        return this;
    }

    public VehicleModelFilter setModifyDateStartWith(Instant modifyDateStartWith) {
        this.modifyDateStartWith = modifyDateStartWith;
        return this;
    }

    public VehicleModelFilter setModifyDateEndWith(Instant modifyDateEndWith) {
        this.modifyDateEndWith = modifyDateEndWith;
        return this;
    }

    public VehicleModelFilter setVehicleBrandId(Long vehicleBrandId) {
        this.vehicleBrandId = vehicleBrandId;
        return this;
    }

    public VehicleModelFilter setVehicleBrandName(String vehicleBrandName) {
        this.vehicleBrandName = vehicleBrandName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VehicleModelFilter that = (VehicleModelFilter) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(vehicleBrandId, that.vehicleBrandId))
            return false;
        if (!Objects.equals(vehicleBrandName, that.vehicleBrandName))
            return false;
        if (!Objects.equals(createDateStartWith, that.createDateStartWith))
            return false;
        if (!Objects.equals(createDateEndWith, that.createDateEndWith))
            return false;
        if (!Objects.equals(modifyDateStartWith, that.modifyDateStartWith))
            return false;
        return Objects.equals(modifyDateEndWith, that.modifyDateEndWith);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vehicleBrandId != null ? vehicleBrandId.hashCode() : 0);
        result = 31 * result + (vehicleBrandName != null ? vehicleBrandName.hashCode() : 0);
        result = 31 * result + (createDateStartWith != null ? createDateStartWith.hashCode() : 0);
        result = 31 * result + (createDateEndWith != null ? createDateEndWith.hashCode() : 0);
        result = 31 * result + (modifyDateStartWith != null ? modifyDateStartWith.hashCode() : 0);
        result = 31 * result + (modifyDateEndWith != null ? modifyDateEndWith.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VehicleModelFilter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vehicleBrandId=" + vehicleBrandId +
                ", vehicleBrandName='" + vehicleBrandName + '\'' +
                ", createDateStartWith=" + createDateStartWith +
                ", createDateEndWith=" + createDateEndWith +
                ", modifyDateStartWith=" + modifyDateStartWith +
                ", modifyDateEndWith=" + modifyDateEndWith +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private Long vehicleBrandId;
        private String vehicleBrandName;
        private Instant createDateStartWith;
        private Instant createDateEndWith;
        private Instant modifyDateStartWith;
        private Instant modifyDateEndWith;
        private Instant removeDateStartWith;
        private Instant removeDateEndWith;

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

        public Builder vehicleBrandId(Long vehicleBrandId) {
            this.vehicleBrandId = vehicleBrandId;
            return this;
        }

        public Builder vehicleBrandName(String vehicleBrandName) {
            this.vehicleBrandName = vehicleBrandName;
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

        public VehicleModelFilter build() {
            return new VehicleModelFilter(this);
        }
    }
}
