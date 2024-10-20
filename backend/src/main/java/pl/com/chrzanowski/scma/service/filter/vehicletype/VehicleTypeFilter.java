package pl.com.chrzanowski.scma.service.filter.vehicletype;

import java.time.Instant;
import java.util.Objects;

public class VehicleTypeFilter {

    private Long id;
    private String name;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;

    public VehicleTypeFilter() {
    }

    private VehicleTypeFilter(Builder builder) {
        id = builder.id;
        name = builder.name;
        createDateStartWith = builder.createDateStartWith;
        createDateEndWith = builder.createDateEndWith;
        modifyDateStartWith = builder.modifyDateStartWith;
        modifyDateEndWith = builder.modifyDateEndWith;
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

    public VehicleTypeFilter setId(Long id) {
        this.id = id;
        return this;
    }

    public VehicleTypeFilter setName(String name) {
        this.name = name;
        return this;
    }

    public VehicleTypeFilter setCreateDateStartWith(Instant createDateStartWith) {
        this.createDateStartWith = createDateStartWith;
        return this;
    }

    public VehicleTypeFilter setCreateDateEndWith(Instant createDateEndWith) {
        this.createDateEndWith = createDateEndWith;
        return this;
    }

    public VehicleTypeFilter setModifyDateStartWith(Instant modifyDateStartWith) {
        this.modifyDateStartWith = modifyDateStartWith;
        return this;
    }

    public VehicleTypeFilter setModifyDateEndWith(Instant modifyDateEndWith) {
        this.modifyDateEndWith = modifyDateEndWith;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VehicleTypeFilter that = (VehicleTypeFilter) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
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
        result = 31 * result + (createDateStartWith != null ? createDateStartWith.hashCode() : 0);
        result = 31 * result + (createDateEndWith != null ? createDateEndWith.hashCode() : 0);
        result = 31 * result + (modifyDateStartWith != null ? modifyDateStartWith.hashCode() : 0);
        result = 31 * result + (modifyDateEndWith != null ? modifyDateEndWith.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VehicleTypeFilter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createDateStartWith=" + createDateStartWith +
                ", createDateEndWith=" + createDateEndWith +
                ", modifyDateStartWith=" + modifyDateStartWith +
                ", modifyDateEndWith=" + modifyDateEndWith +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
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

        public Builder name(String name) {
            this.name = name;
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

        public VehicleTypeFilter build() {
            return new VehicleTypeFilter(this);
        }
    }
}
