package pl.com.chrzanowski.scma.service.filter.vehiclemodel;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.VehicleBrand;
import pl.com.chrzanowski.scma.domain.VehicleModel;

import java.time.Instant;

@Component
public class VehicleModelSpecification {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BRAND = "vehicleBrand";
    public static final String BRAND_ID = "id";
    public static final String BRAND_NAME = "name";
    public static final String CREATE_DATE = "createDate";
    public static final String MODIFY_DATE = "modifyDate";

    public static Specification<VehicleModel> createSpecification(VehicleModelFilter vehicleModelFilter) {
        Specification<VehicleModel> specification = Specification.where(null);
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
            if (vehicleModelFilter.getVehicleBrandId() != null) {
                specification = specification.and(hasBrandId(vehicleModelFilter.getVehicleBrandId()));
            }
            if (vehicleModelFilter.getVehicleBrandName() != null) {
                specification = specification.and(hasBrandName(vehicleModelFilter.getVehicleBrandName()));
            }
        }
        return specification;
    }

    private static Specification<VehicleModel> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<VehicleModel> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<VehicleModel> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<VehicleModel> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<VehicleModel> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<VehicleModel> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<VehicleModel> hasBrandId(Long brandId) {
        return (root, query, criteriaBuilder) -> {
            Join<VehicleBrand, VehicleModel> vehicleModelBrand = root.join(BRAND);
            return criteriaBuilder.equal(vehicleModelBrand.get(BRAND_ID), brandId);
        };
    }

    private static Specification<VehicleModel> hasBrandName(String brandName) {
        return (root, query, criteriaBuilder) -> {
            Join<VehicleBrand, VehicleModel> vehicleModelBrand = root.join(BRAND);
            return criteriaBuilder.like(vehicleModelBrand.get(BRAND_NAME), "%" + brandName + "%");
        };
    }


}
