package pl.com.chrzanowski.scma.service.dto;

import pl.com.chrzanowski.scma.domain.enumeration.TypeOfService;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class ServiceActionTypeDTO {

    private Long id;
    private String name;
    private TypeOfService typeOfService;
    private Instant createDate;
    private Instant modifyDate;
    private Instant removeDate;

    private Set<WorkshopDTO> workshops;

    public ServiceActionTypeDTO() {
    }

    public ServiceActionTypeDTO(Long id,
                                String name,
                                TypeOfService typeOfService,
                                Instant createDate,
                                Instant modifyDate,
                                Instant removeDate,
                                Set<WorkshopDTO> workshops) {
        this.id = id;
        this.name = name;
        this.typeOfService = typeOfService;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.removeDate = removeDate;
        this.workshops = workshops;
    }

    private ServiceActionTypeDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setTypeOfService(builder.typeOfService);
        setCreateDate(builder.createDate);
        setModifyDate(builder.modifyDate);
        setRemoveDate(builder.removeDate);
        setWorkshops(builder.workshops);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ServiceActionTypeDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.typeOfService = copy.getTypeOfService();
        builder.createDate = copy.getCreateDate();
        builder.modifyDate = copy.getModifyDate();
        builder.removeDate = copy.getRemoveDate();
        builder.workshops = copy.getWorkshops();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public ServiceActionTypeDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceActionTypeDTO setName(String name) {
        this.name = name;
        return this;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
    }

    public ServiceActionTypeDTO setTypeOfService(TypeOfService typeOfService) {
        this.typeOfService = typeOfService;
        return this;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public ServiceActionTypeDTO setCreateDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public ServiceActionTypeDTO setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public Instant getRemoveDate() {
        return removeDate;
    }

    public ServiceActionTypeDTO setRemoveDate(Instant removeDate) {
        this.removeDate = removeDate;
        return this;
    }

    public Set<WorkshopDTO> getWorkshops() {
        return workshops;
    }

    public ServiceActionTypeDTO setWorkshops(Set<WorkshopDTO> workshops) {
        this.workshops = workshops;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceActionTypeDTO that = (ServiceActionTypeDTO) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (typeOfService != that.typeOfService) return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        if (!Objects.equals(modifyDate, that.modifyDate)) return false;
        if (!Objects.equals(removeDate, that.removeDate)) return false;
        return Objects.equals(workshops, that.workshops);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (typeOfService != null ? typeOfService.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        result = 31 * result + (removeDate != null ? removeDate.hashCode() : 0);
        result = 31 * result + (workshops != null ? workshops.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceActionTypeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeOfService=" + typeOfService +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", removeDate=" + removeDate +
                ", workshops=" + workshops +
                '}';
    }


    public static final class Builder {
        private Long id;
        private String name;
        private TypeOfService typeOfService;
        private Instant createDate;
        private Instant modifyDate;
        private Instant removeDate;
        private Set<WorkshopDTO> workshops;

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

        public Builder workshops(Set<WorkshopDTO> workshops) {
            this.workshops = workshops;
            return this;
        }

        public ServiceActionTypeDTO build() {
            return new ServiceActionTypeDTO(this);
        }
    }
}
