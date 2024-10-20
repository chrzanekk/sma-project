package pl.com.chrzanowski.scma.service.filter.serviceaction;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.ServiceAction;
import pl.com.chrzanowski.scma.domain.Vehicle;
import pl.com.chrzanowski.scma.domain.Workshop;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Component
public class ServiceActionSpecification {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CAR_MILEAGE = "carMileage";
    private static final String INVOICE_NUMBER = "invoiceNumber";
    private static final String GROSS_VALUE = "grossValue";
    private static final String TAX_VALUE = "taxValue";
    private static final String NET_VALUE = "netValue";
    private static final String TAX_RATE = "taxRate";
    private static final String WORKSHOP = "workshop";
    private static final String VEHICLE = "vehicle";
    private static final String WORKSHOP_ID = "workshopId";
    private static final String WORKSHOP_NAME = "workshopName";
    private static final String VEHICLE_ID = "vehicleId";
    private static final String VEHICLE_REGISTRATION_NUMBER = "registrationNumber";
    private static final String SERVICE_DATE = "serviceDate";
    private static final String DESCRIPTION = "description";
    private static final String CREATE_DATE = "createDate";
    private static final String MODIFY_DATE = "modifyDate";


    public static Specification<ServiceAction> createSpecification(ServiceActionFilter serviceActionFilter) {
        Specification<ServiceAction> specification = Specification.where(null);
        if (serviceActionFilter != null) {
            if (serviceActionFilter.getId() != null) {
                specification = specification.and(hasId(serviceActionFilter.getId()));
            }
            if (serviceActionFilter.getCarMileageStartsWith() != null) {
                specification =
                        specification.and(hasIntegerTypeStartWith(serviceActionFilter.getCarMileageStartsWith()));
            }
            if (serviceActionFilter.getCarMileageEndWith() != null) {
                specification =
                        specification.and(hasIntegerTypeEndWith(serviceActionFilter.getCarMileageEndWith()));
            }
            if (serviceActionFilter.getInvoiceNumber() != null) {
                specification = specification.and(hasString(serviceActionFilter.getInvoiceNumber(), INVOICE_NUMBER));
            }
            if (serviceActionFilter.getGrossValueStartsWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueStartWith(serviceActionFilter.getGrossValueStartsWith(),
                                GROSS_VALUE));
            }
            if (serviceActionFilter.getGrossValueEndWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueEndWith(serviceActionFilter.getGrossValueEndWith(),
                                GROSS_VALUE));
            }
            if (serviceActionFilter.getTaxValueStartsWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueStartWith(serviceActionFilter.getTaxValueStartsWith(),
                                TAX_VALUE));
            }
            if (serviceActionFilter.getTaxValueEndWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueEndWith(serviceActionFilter.getTaxValueEndWith(),
                                TAX_VALUE));
            }
            if (serviceActionFilter.getNetValueStartsWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueStartWith(serviceActionFilter.getNetValueStartsWith(),
                                NET_VALUE));
            }
            if (serviceActionFilter.getNetValueEndWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueEndWith(serviceActionFilter.getNetValueEndWith(),
                                NET_VALUE));
            }
            if (serviceActionFilter.getTaxRateStartsWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueStartWith(serviceActionFilter.getTaxRateStartsWith(),
                                TAX_RATE));
            }
            if (serviceActionFilter.getTaxRateEndWith() != null) {
                specification =
                        specification.and(hasBigDecimalValueEndWith(serviceActionFilter.getTaxRateEndWith(),
                                TAX_RATE));
            }
            if (serviceActionFilter.getServiceDateStartsWith() != null) {
                specification =
                        specification.and(hasServiceDateStarsWith(serviceActionFilter.getServiceDateStartsWith()));
            }
            if (serviceActionFilter.getServiceDateEndWith() != null) {
                specification = specification.and(hasServiceDateEndWith(serviceActionFilter.getServiceDateEndWith()));
            }
            if (serviceActionFilter.getDescription() != null) {
                specification = specification.and(hasString(serviceActionFilter.getDescription(), DESCRIPTION));
            }
            if (serviceActionFilter.getCreateDateStartWith() != null) {
                specification =
                        specification.and(hasInstantDateStartWith(serviceActionFilter.getCreateDateStartWith(),
                                CREATE_DATE));
            }
            if (serviceActionFilter.getCreateDateEndWith() != null) {
                specification =
                        specification.and(hasInstantDateEndWith(serviceActionFilter.getCreateDateEndWith(),
                                CREATE_DATE));
            }
            if (serviceActionFilter.getModifyDateStartWith() != null) {
                specification =
                        specification.and(hasInstantDateStartWith(serviceActionFilter.getModifyDateStartWith(),
                                MODIFY_DATE));
            }
            if (serviceActionFilter.getModifyDateEndWith() != null) {
                specification =
                        specification.and(hasInstantDateEndWith(serviceActionFilter.getModifyDateEndWith(),
                                MODIFY_DATE));
            }
            if (serviceActionFilter.getWorkshopId() != null) {
                specification = specification.and(hasWorkshopId(serviceActionFilter.getWorkshopId()));
            }
            if (serviceActionFilter.getWorkshopName() != null) {
                specification = specification.and(hasWorkshopName(serviceActionFilter.getWorkshopName()));
            }
            if (serviceActionFilter.getVehicleId() != null) {
                specification = specification.and(hasVehicleId(serviceActionFilter.getVehicleId()));
            }
            if (serviceActionFilter.getVehicleRegistrationNumber() != null) {
                specification =
                        specification.and(hasVehicleRegistrationNumber(serviceActionFilter.getVehicleRegistrationNumber()));
            }
        }
        return specification;
    }

    private static Specification<ServiceAction> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<ServiceAction> hasString(String text, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(fieldType), "%" + text + "%");
    }

    private static Specification<ServiceAction> hasIntegerTypeStartWith(Integer value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(CAR_MILEAGE), value);
    }

    private static Specification<ServiceAction> hasIntegerTypeEndWith(Integer value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(CAR_MILEAGE), value);
    }

    private static Specification<ServiceAction> hasInstantDateStartWith(Instant date, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldType),
                date);
    }

    private static Specification<ServiceAction> hasInstantDateEndWith(Instant date, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldType),
                date);
    }

    private static Specification<ServiceAction> hasServiceDateStarsWith(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(SERVICE_DATE),
                date);
    }

    private static Specification<ServiceAction> hasServiceDateEndWith(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(SERVICE_DATE),
                date);
    }

    private static Specification<ServiceAction> hasBigDecimalValueStartWith(BigDecimal dimension, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldType), dimension);
    }

    private static Specification<ServiceAction> hasBigDecimalValueEndWith(BigDecimal dimension, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldType), dimension);
    }

    private static Specification<ServiceAction> hasWorkshopId(Long id) {
        return (root, query, criteriaBuilder) -> {
            Join<Workshop, ServiceAction> workshopServiceActionJoin = root.join(WORKSHOP);
            return criteriaBuilder.equal(workshopServiceActionJoin.get(ID), id);
        };
    }

    private static Specification<ServiceAction> hasWorkshopName(String name) {
        return (root, query, criteriaBuilder) -> {
            Join<Workshop, ServiceAction> workshopServiceActionJoin = root.join(WORKSHOP);
            return criteriaBuilder.like(workshopServiceActionJoin.get(NAME), "%" + name + "%");
        };
    }

    private static Specification<ServiceAction> hasVehicleId(Long id) {
        return (root, query, criteriaBuilder) -> {
            Join<Vehicle, ServiceAction> vehicleServiceActionJoin =
                    root.join(VEHICLE);
            return criteriaBuilder.equal(vehicleServiceActionJoin.get(ID), id);
        };
    }

    private static Specification<ServiceAction> hasVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        return ((root, query, criteriaBuilder) -> {
            Join<Vehicle, ServiceAction> vehicleServiceActionJoin = root.join(VEHICLE);
            return criteriaBuilder.like(vehicleServiceActionJoin.get(VEHICLE_REGISTRATION_NUMBER),
                    "%" + vehicleRegistrationNumber + "%");
        });
    }
}
