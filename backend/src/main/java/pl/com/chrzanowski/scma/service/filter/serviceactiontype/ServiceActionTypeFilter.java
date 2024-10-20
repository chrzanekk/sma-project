package pl.com.chrzanowski.scma.service.filter.serviceactiontype;

import pl.com.chrzanowski.scma.domain.enumeration.TypeOfService;

import java.time.Instant;
import java.util.Objects;

public class ServiceActionTypeFilter {

    private Long id;
    private String name;
    private TypeOfService typeOfService;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;

    public ServiceActionTypeFilter() {
    }

    private ServiceActionTypeFilter(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setTypeOfService(builder.typeOfService);
        setCreateDateStartWith(builder.createDateStartWith);
        setCreateDateEndWith(builder.createDateEndWith);
        setModifyDateStartWith(builder.modifyDateStartWith);
        setModifyDateEndWith(builder.modifyDateEndWith);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ServiceActionTypeFilter copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.typeOfService = copy.getTypeOfService();
        builder.createDateStartWith = copy.getCreateDateStartWith();
        builder.createDateEndWith = copy.getCreateDateEndWith();
        builder.modifyDateStartWith = copy.getModifyDateStartWith();
        builder.modifyDateEndWith = copy.getModifyDateEndWith();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
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

    public ServiceActionTypeFilter setId(Long id) {
        this.id = id;
        return this;
    }

    public ServiceActionTypeFilter setName(String name) {
        this.name = name;
        return this;
    }

    public ServiceActionTypeFilter setTypeOfService(TypeOfService typeOfService) {
        this.typeOfService = typeOfService;
        return this;
    }

    public ServiceActionTypeFilter setCreateDateStartWith(Instant createDateStartWith) {
        this.createDateStartWith = createDateStartWith;
        return this;
    }

    public ServiceActionTypeFilter setCreateDateEndWith(Instant createDateEndWith) {
        this.createDateEndWith = createDateEndWith;
        return this;
    }

    public ServiceActionTypeFilter setModifyDateStartWith(Instant modifyDateStartWith) {
        this.modifyDateStartWith = modifyDateStartWith;
        return this;
    }

    public ServiceActionTypeFilter setModifyDateEndWith(Instant modifyDateEndWith) {
        this.modifyDateEndWith = modifyDateEndWith;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceActionTypeFilter that = (ServiceActionTypeFilter) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (typeOfService != that.typeOfService) return false;
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
        result = 31 * result + (typeOfService != null ? typeOfService.hashCode() : 0);
        result = 31 * result + (createDateStartWith != null ? createDateStartWith.hashCode() : 0);
        result = 31 * result + (createDateEndWith != null ? createDateEndWith.hashCode() : 0);
        result = 31 * result + (modifyDateStartWith != null ? modifyDateStartWith.hashCode() : 0);
        result = 31 * result + (modifyDateEndWith != null ? modifyDateEndWith.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceActionTypeFilter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeOfService=" + typeOfService +
                ", createDateStartWith=" + createDateStartWith +
                ", createDateEndWith=" + createDateEndWith +
                ", modifyDateStartWith=" + modifyDateStartWith +
                ", modifyDateEndWith=" + modifyDateEndWith +
                '}';
    }


    public static final class Builder {
        private Long id;
        private String name;
        private TypeOfService typeOfService;
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

        public Builder typeOfService(TypeOfService typeOfService) {
            this.typeOfService = typeOfService;
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

        public ServiceActionTypeFilter build() {
            return new ServiceActionTypeFilter(this);
        }
    }
}
