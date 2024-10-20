package pl.com.chrzanowski.scma.service.filter.vehicletype;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.VehicleType;

import java.time.Instant;

@Component
public class VehicleTypeSpecification {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATE_DATE = "createDate";
    public static final String MODIFY_DATE = "modifyDate";
    public static final String REMOVE_DATE = "removeDate";

    public static Specification<VehicleType> createSpecification(VehicleTypeFilter vehicleTypeFilter) {
        Specification<VehicleType> specification = Specification.where(null);
        if (vehicleTypeFilter != null) {
            if (vehicleTypeFilter.getId() != null) {
                specification = specification.and(hasId(vehicleTypeFilter.getId()));
            }
            if (vehicleTypeFilter.getName() != null) {
                specification = specification.and(hasName(vehicleTypeFilter.getName()));
            }
            if (vehicleTypeFilter.getCreateDateStartWith() != null) {
                specification = specification.and(hasCreateDateStartWith(vehicleTypeFilter.getCreateDateStartWith()));
            }
            if (vehicleTypeFilter.getCreateDateEndWith() != null) {
                specification = specification.and(hasCreateDateEndWith(vehicleTypeFilter.getCreateDateEndWith()));
            }
            if (vehicleTypeFilter.getModifyDateStartWith() != null) {
                specification = specification.and(hasModifyDateStartWith(vehicleTypeFilter.getModifyDateStartWith()));
            }
            if (vehicleTypeFilter.getModifyDateEndWith() != null) {
                specification = specification.and(hasModifyDateEndWith(vehicleTypeFilter.getModifyDateEndWith()));
            }
        }
        return specification;
    }

    private static Specification<VehicleType> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<VehicleType> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<VehicleType> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<VehicleType> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<VehicleType> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<VehicleType> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }
}
