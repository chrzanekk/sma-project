package pl.com.chrzanowski.sma.contractor.service.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.time.Instant;

@Component
public class ContractorSpecification {
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

    public static Specification<Contractor> createSpecification(ContractorFilter contractorFilter) {
        Specification<Contractor> specification = Specification.where(null);
        if (contractorFilter != null) {
            if (contractorFilter.getId() != null) {
                specification = specification.and(hasId(contractorFilter.getId()));
            }
            if (contractorFilter.getName() != null) {
                specification = specification.and(hasName(contractorFilter.getName()));
            }
            if (contractorFilter.getTaxNumber() != null) {
                specification = specification.and(hasTaxNumber(contractorFilter.getTaxNumber()));
            }
            if (contractorFilter.getStreet() != null) {
                specification = specification.and(hasStreet(contractorFilter.getStreet()));
            }
            if (contractorFilter.getBuildingNo() != null) {
                specification = specification.and(hasBuildingNo(contractorFilter.getBuildingNo()));
            }
            if (contractorFilter.getApartmentNo() != null) {
                specification = specification.and(hasApartmentNo(contractorFilter.getApartmentNo()));
            }
            if (contractorFilter.getPostalCode() != null) {
                specification = specification.and(hasPostalCode(contractorFilter.getPostalCode()));
            }
            if (contractorFilter.getCity() != null) {
                specification = specification.and(hasCity(contractorFilter.getCity()));
            }
            if (contractorFilter.getCountry() != null) {
                specification = specification.and(hasCountry(contractorFilter.getCountry()));
            }
            if (contractorFilter.getCreateDateStartWith() != null) {
                specification = specification.and(hasCreateDateStartWith(contractorFilter.getCreateDateStartWith()));
            }
            if (contractorFilter.getCreateDateEndWith() != null) {
                specification = specification.and(hasCreateDateEndWith(contractorFilter.getCreateDateEndWith()));
            }
            if (contractorFilter.getModifyDateStartWith() != null) {
                specification = specification.and(hasModifyDateStartWith(contractorFilter.getModifyDateStartWith()));
            }
            if (contractorFilter.getModifyDateEndWith() != null) {
                specification = specification.and(hasModifyDateEndWith(contractorFilter.getModifyDateEndWith()));
            }
        }
        return specification;
    }

    private static Specification<Contractor> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<Contractor> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), "%" + name + "%");
    }

    private static Specification<Contractor> hasTaxNumber(String taxNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(TAX_NUMBER), "%" + taxNumber + "%");
    }

    private static Specification<Contractor> hasStreet(String street) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(STREET), "%" + street + "%");
    }

    private static Specification<Contractor> hasBuildingNo(String buildingNo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(BUILDING_NO), "%" + buildingNo + "%");
    }

    private static Specification<Contractor> hasApartmentNo(String apartmentNo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(APARTMENT_NO), "%" + apartmentNo + "%");
    }

    private static Specification<Contractor> hasPostalCode(String postalCode) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(POSTAL_CODE), "%" + postalCode + "%");
    }

    private static Specification<Contractor> hasCity(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(CITY), "%" + city + "%");
    }

    private static Specification<Contractor> hasCountry(Country country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(COUNTRY), country);
    }

    private static Specification<Contractor> hasCreateDateStartWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<Contractor> hasCreateDateEndWith(Instant createDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CREATE_DATE),
                createDate);
    }

    private static Specification<Contractor> hasModifyDateStartWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }

    private static Specification<Contractor> hasModifyDateEndWith(Instant modifyDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(MODIFY_DATE),
                modifyDate);
    }


}
