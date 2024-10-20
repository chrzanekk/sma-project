package pl.com.chrzanowski.scma.service.filter.vehiclebrand;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.VehicleBrand;

import java.time.Instant;

@Component
public class VehicleBrandSpecification {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATE_DATE = "createDate";
    public static final String MODIFY_DATE = "modifyDate";

    public static Specification<VehicleBrand> createSpecification(VehicleBrandFilter vehicleModelFilter) {
        Specification<VehicleBrand> specification = Specification.where(null);
        if (vehicleModelFilter != null) {
            if (vehicleModelFilter.getId() != null) {
                specification = specification.and(hasId(vehicleModelFilter.getId()));
            }
            if (vehicleModelFilter.getName() != null) {
                specification = specification.and(hasName(vehicleModelFilter.getName()));
            }
            if (vehicleModelFilter.getCreateDateStartWith() != null) {
                specification = specification.and(hasCreateDateStartWith(vehicleModelFilter.getCreateDateStartWith()));
            }
            if (vehicleModelFilter.getCreateDateEndWith() != null) {
                specification = specification.and(hasCreateDateEndWith(vehicleModelFilter.getCreateDateEndWith()));
            }
            if (vehicleModelFilter.getModifyDateStartWith() != null) {
                specification = specification.and(hasModifyDateStartWith(vehicleModelFilter.getModifyDateStartWith()));
            }
            if (vehicleModelFilter.getModifyDateEndWith() != null) {
                specification = specification.and(hasModifyDateEndWith(vehicleModelFilter.getModifyDateEndWith()));
            }
        }
        return specification;
    }

    private static Specification<VehicleBrand> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<VehicleBrand> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<VehicleBrand> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<VehicleBrand> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<VehicleBrand> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<VehicleBrand> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }
}
