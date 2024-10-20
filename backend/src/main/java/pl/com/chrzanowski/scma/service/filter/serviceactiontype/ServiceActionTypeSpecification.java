package pl.com.chrzanowski.scma.service.filter.serviceactiontype;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.ServiceActionType;
import pl.com.chrzanowski.scma.domain.enumeration.TypeOfService;

import java.time.Instant;

@Component
public class ServiceActionTypeSpecification {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE_OF_SERVICE = "typeOfService";
    public static final String CREATE_DATE = "createDate";
    public static final String MODIFY_DATE = "modifyDate";

    public static Specification<ServiceActionType> createSpecification(ServiceActionTypeFilter serviceActionTypeFilter) {
        Specification<ServiceActionType> specification = Specification.where(null);
        if (serviceActionTypeFilter != null) {
            if (serviceActionTypeFilter.getId() != null) {
                specification = specification.and(hasId(serviceActionTypeFilter.getId()));
            }
            if (serviceActionTypeFilter.getName() != null) {
                specification = specification.and(hasName(serviceActionTypeFilter.getName()));
            }
            if (serviceActionTypeFilter.getTypeOfService() != null) {
                specification = specification.and(hasTypeOfService(serviceActionTypeFilter.getTypeOfService()));
            }
            if (serviceActionTypeFilter.getCreateDateStartWith() != null) {
                specification = specification.and(hasCreateDateStartWith(serviceActionTypeFilter.getCreateDateStartWith()));
            }
            if (serviceActionTypeFilter.getCreateDateEndWith() != null) {
                specification = specification.and(hasCreateDateEndWith(serviceActionTypeFilter.getCreateDateEndWith()));
            }
            if (serviceActionTypeFilter.getModifyDateStartWith() != null) {
                specification = specification.and(hasModifyDateStartWith(serviceActionTypeFilter.getModifyDateStartWith()));
            }
            if (serviceActionTypeFilter.getModifyDateEndWith() != null) {
                specification = specification.and(hasModifyDateEndWith(serviceActionTypeFilter.getModifyDateEndWith()));
            }
        }
        return specification;
    }

    private static Specification<ServiceActionType> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<ServiceActionType> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<ServiceActionType> hasTypeOfService(TypeOfService typeOfService) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TYPE_OF_SERVICE), typeOfService);
    }

    private static Specification<ServiceActionType> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<ServiceActionType> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<ServiceActionType> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<ServiceActionType> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }
}
