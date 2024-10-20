package pl.com.chrzanowski.scma.service.dto;

import java.time.Instant;
import java.util.Objects;

public class VehicleBrandDTO {
    private  Long id;
    private  String name;
    private  Instant createDate;
    private  Instant modifyDate;
    private  Instant removeDate;

    public VehicleBrandDTO() {
    }

    public VehicleBrandDTO(Long id, String name, Instant createDate, Instant modifyDate, Instant removeDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
    }

    private VehicleBrandDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        createDate = builder.createDate;
        modifyDate = builder.modifyDate;
        removeDate = builder.removeDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(VehicleBrandDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.createDate = copy.getCreateDate();
        builder.modifyDate = copy.getModifyDate();
        builder.removeDate = copy.getRemoveDate();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleBrandDTO that = (VehicleBrandDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(createDate, that.createDate) && Objects.equals(modifyDate, that.modifyDate) && Objects.equals(removeDate, that.removeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createDate, modifyDate, removeDate);
    }

    @Override
    public String toString() {
        return "VehicleBrandDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                '}';
    }


    public static  class Builder {
        private Long id;
        private String name;
        private Instant createDate;
        private Instant modifyDate;
        private Instant removeDate;

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

        public VehicleBrandDTO build() {
            return new VehicleBrandDTO(this);
        }
    }
}
