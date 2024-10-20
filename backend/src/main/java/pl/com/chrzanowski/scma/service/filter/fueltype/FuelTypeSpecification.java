package pl.com.chrzanowski.scma.service.filter.fueltype;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.FuelType;

import java.time.Instant;

@Component
public class FuelTypeSpecification {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATE_DATE = "createDate";
    public static final String MODIFY_DATE = "modifyDate";

    public static Specification<FuelType> createSpecification(FuelTypeFilter fuelTypeFilter) {
        Specification<FuelType> specification = Specification.where(null);
        if (fuelTypeFilter != null) {
            if (fuelTypeFilter.getId() != null) {
                specification = specification.and(hasId(fuelTypeFilter.getId()));
            }
            if (fuelTypeFilter.getName() != null) {
                specification = specification.and(hasName(fuelTypeFilter.getName()));
            }
            if (fuelTypeFilter.getCreateDateStartWith() != null) {
                specification = specification.and(hasCreateDateStartWith(fuelTypeFilter.getCreateDateStartWith()));
            }
            if (fuelTypeFilter.getCreateDateEndWith() != null) {
                specification = specification.and(hasCreateDateEndWith(fuelTypeFilter.getCreateDateEndWith()));
            }
            if (fuelTypeFilter.getModifyDateStartWith() != null) {
                specification = specification.and(hasModifyDateStartWith(fuelTypeFilter.getModifyDateStartWith()));
            }
            if (fuelTypeFilter.getModifyDateEndWith() != null) {
                specification = specification.and(hasModifyDateEndWith(fuelTypeFilter.getModifyDateEndWith()));
            }
        }
        return specification;
    }

    private static Specification<FuelType> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<FuelType> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<FuelType> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<FuelType> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<FuelType> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<FuelType> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }
}
