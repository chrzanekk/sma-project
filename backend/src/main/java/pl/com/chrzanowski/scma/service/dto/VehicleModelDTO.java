package pl.com.chrzanowski.scma.service.dto;

import java.time.Instant;
import java.util.Objects;


public class VehicleModelDTO {
    private Long id;
    private String name;
    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;
    private Long vehicleBrandId;
    private String vehicleBrandName;

    public VehicleModelDTO() {
    }

    public VehicleModelDTO(Long id,
                           String name,
                           Instant createDate,
                           Instant modifyDate,
                           Instant removeDate,
                           Long vehicleBrandId,
                           String vehicleBrandName) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
        this.vehicleBrandId = vehicleBrandId;
        this.vehicleBrandName = vehicleBrandName;
    }

    private VehicleModelDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        createDate = builder.createDate;
        modifyDate = builder.modifyDate;
        removeDate = builder.removeDate;
        vehicleBrandId = builder.vehicleBrandId;
        vehicleBrandName = builder.vehicleBrandName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(VehicleModelDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.createDate = copy.getCreateDate();
        builder.modifyDate = copy.getModifyDate();
        builder.removeDate = copy.getRemoveDate();
        builder.vehicleBrandId = copy.getVehicleBrandId();
        builder.vehicleBrandName = copy.getVehicleBrandName();
        return builder;
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

    public Long getVehicleBrandId() {
        return vehicleBrandId;
    }

    public String getVehicleBrandName() {
        return vehicleBrandName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VehicleModelDTO that = (VehicleModelDTO) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        if (!Objects.equals(modifyDate, that.modifyDate)) return false;
        if (!Objects.equals(removeDate, that.removeDate)) return false;
        if (!Objects.equals(vehicleBrandId, that.vehicleBrandId)) return false;
        return Objects.equals(vehicleBrandName, that.vehicleBrandName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        result = 31 * result + (vehicleBrandId != null ? vehicleBrandId.hashCode() : 0);
        result = 31 * result + (vehicleBrandName != null ? vehicleBrandName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VehicleModelDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                ", brandId=" + vehicleBrandId +
                ", brandName=" + vehicleBrandName +
                '}';
    }


    public static final class Builder {
        private Long id;
        private String name;
        private Instant createDate;
        private Instant modifyDate;
        private Instant removeDate;
        private Long vehicleBrandId;
        private String vehicleBrandName;

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

        public Builder vehicleBrandId(Long vehicleBrandId) {
            this.vehicleBrandId = vehicleBrandId;
            return this;
        }

        public Builder vehicleBrandName(String vehicleBrandName) {
            this.vehicleBrandName = vehicleBrandName;
            return this;
        }

        public VehicleModelDTO build() {
            return new VehicleModelDTO(this);
        }
    }
}
