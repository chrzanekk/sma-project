package pl.com.chrzanowski.sma.workshop;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.enumeration.Country;

import java.time.Instant;

@Component
public class WorkshopSpecification {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TAX_NUMBER = "taxNumber";
    private static final String STREET = "street";
    private static final String BUILDING_NO = "buildingNo";
    private static final String APARTMENT_NO = "apartmentNo";
    private static final String POSTAL_CODE = "postalCode";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String CREATE_DATE = "createDate";
    private static final String MODIFY_DATE = "modifyDate";

    public static Specification<Workshop> createSpecification(WorkshopFilter workshopFilter) {
        Specification<Workshop> specification = Specification.where(null);
        if (workshopFilter != null) {
            if (workshopFilter.getId() != null) {
                specification = specification.and(hasId(workshopFilter.getId()));
            }
            if (workshopFilter.getName() != null) {
                specification = specification.and(hasName(workshopFilter.getName()));
            }
            if (workshopFilter.getTaxNumber() != null) {
                specification = specification.and(hasTaxNumber(workshopFilter.getTaxNumber()));
            }
            if (workshopFilter.getStreet() != null) {
                specification = specification.and(hasStreet(workshopFilter.getStreet()));
            }
            if (workshopFilter.getBuildingNo() != null) {
                specification = specification.and(hasBuildingNo(workshopFilter.getBuildingNo()));
            }
            if (workshopFilter.getApartmentNo() != null) {
                specification = specification.and(hasApartmentNo(workshopFilter.getApartmentNo()));
            }
            if (workshopFilter.getPostalCode() != null) {
                specification = specification.and(hasPostalCode(workshopFilter.getPostalCode()));
            }
            if (workshopFilter.getCity() != null) {
                specification = specification.and(hasCity(workshopFilter.getCity()));
            }
            if (workshopFilter.getCountry() != null) {
                specification = specification.and(hasCountry(workshopFilter.getCountry()));
            }
            if (workshopFilter.getCreateDateStartWith() != null) {
                specification = specification.and(hasCreateDateStartWith(workshopFilter.getCreateDateStartWith()));
            }
            if (workshopFilter.getCreateDateEndWith() != null) {
                specification = specification.and(hasCreateDateEndWith(workshopFilter.getCreateDateEndWith()));
            }
            if (workshopFilter.getModifyDateStartWith() != null) {
                specification = specification.and(hasModifyDateStartWith(workshopFilter.getModifyDateStartWith()));
            }
            if (workshopFilter.getModifyDateEndWith() != null) {
                specification = specification.and(hasModifyDateEndWith(workshopFilter.getModifyDateEndWith()));
            }
        }
        return specification;
    }

    private static Specification<Workshop> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<Workshop> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<Workshop> hasTaxNumber(String taxNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(TAX_NUMBER), "%" + taxNumber + "%");
    }

    private static Specification<Workshop> hasStreet(String street) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(STREET), "%" + street + "%");
    }

    private static Specification<Workshop> hasBuildingNo(String buildingNo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(BUILDING_NO), "%" + buildingNo + "%");
    }

    private static Specification<Workshop> hasApartmentNo(String apartmentNo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(APARTMENT_NO), "%" + apartmentNo + "%");
    }

    private static Specification<Workshop> hasPostalCode(String postalCode) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(POSTAL_CODE), "%" + postalCode + "%");
    }

    private static Specification<Workshop> hasCity(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(CITY), "%" + city + "%");
    }

    private static Specification<Workshop> hasCountry(Country country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(COUNTRY), country);
    }

    private static Specification<Workshop> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<Workshop> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<Workshop> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<Workshop> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }


}
