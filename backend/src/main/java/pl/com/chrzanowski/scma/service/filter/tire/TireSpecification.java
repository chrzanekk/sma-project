package pl.com.chrzanowski.scma.service.filter.tire;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.domain.Tire;
import pl.com.chrzanowski.scma.domain.Vehicle;
import pl.com.chrzanowski.scma.domain.enumeration.*;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class TireSpecification {

    private static final String ID = "id";
    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final String WIDTH = "width";
    private static final String PROFILE = "profile";
    private static final String DIAMETER = "diameter";
    private static final String TYPE = "type";
    private static final String TIRE_REINFORCED_INDEX = "tireReinforcedIndex";
    private static final String SPEED_INDEX = "speedIndex";
    private static final String CAPACITY_INDEX = "capacityIndex";
    private static final String TIRE_SEASON_TYPE = "tireSeasonType";
    private static final String RUN_ON_FLAT = "runOnFlat";
    private static final String TIRE_STATUS = "tireStatus";
    private static final String VEHICLE = "vehicle";
    private static final String VEHICLE_ID = "vehicleId";
    private static final String PRODUCTION_YEAR = "productionYear";
    private static final String PURCHASE_DATE = "purchaseDate";
    private static final String CREATE_DATE = "createDate";
    private static final String MODIFY_DATE = "modifyDate";


    public static Specification<Tire> create(TireFilter filter) {
        Specification<Tire> specification = Specification.where(null);
        if (filter != null) {
            if (filter.getId() != null) {
                specification = specification.and(hasId(filter.getId()));
            }
            if (filter.getBrand() != null) {
                specification = specification.and(hasString(filter.getBrand(), BRAND));
            }
            if (filter.getModel() != null) {
                specification = specification.and(hasString(filter.getModel(), MODEL));
            }
            if (filter.getWidthStartsWith() != null) {
                specification = specification.and(hasIntegerTypeStartWith(filter.getWidthStartsWith(), WIDTH));
            }
            if (filter.getWidthEndWith() != null) {
                specification = specification.and(hasIntegerTypeEndWith(filter.getWidthEndWith(), WIDTH));
            }
            if (filter.getProfileStartsWith() != null) {
                specification = specification.and(hasIntegerTypeStartWith(filter.getProfileStartsWith(), PROFILE));
            }
            if (filter.getProfileEndWith() != null) {
                specification = specification.and(hasIntegerTypeEndWith(filter.getProfileEndWith(), PROFILE));
            }
            if (filter.getDiameterStartsWith() != null) {
                specification = specification.and(hasIntegerTypeStartWith(filter.getDiameterStartsWith(), DIAMETER));
            }
            if (filter.getDiameterEndWith() != null) {
                specification = specification.and(hasIntegerTypeEndWith(filter.getDiameterEndWith(), DIAMETER));
            }
            if (filter.getType() != null) {
                specification = specification.and(hasTireType(filter.getType()));
            }
            if (filter.getReinforcedIndex() != null) {
                specification = specification.and(hasReinforcedIndex(filter.getReinforcedIndex()));
            }
            if (filter.getSpeedIndex() != null) {
                specification = specification.and(hasSpeedIndex(filter.getSpeedIndex()));
            }
            if (filter.getLoadCapacityIndex() != null) {
                specification = specification.and(hasLoadCapacityIndex(filter.getLoadCapacityIndex()));
            }
            if (filter.getSeasonType() != null) {
                specification = specification.and(hasSeasonType(filter.getSeasonType()));
            }
            if (filter.getRunOnFlat() != null) {
                specification = specification.and(hasRunOnFlat(filter.getRunOnFlat()));
            }
            if (filter.getCreateDateStartsWith() != null) {
                specification = specification.and(hasInstantDateStartWith(filter.getCreateDateStartsWith(),
                        CREATE_DATE));
            }
            if (filter.getCreateDateEndWith() != null) {
                specification = specification.and(hasInstantDateEndWith(filter.getCreateDateStartsWith(),
                        CREATE_DATE));
            }
            if (filter.getModifyDateStartsWith() != null) {
                specification = specification.and(hasInstantDateStartWith(filter.getModifyDateStartsWith(),
                        MODIFY_DATE));
            }
            if (filter.getModifyDateEndWith() != null) {
                specification = specification.and(hasInstantDateEndWith(filter.getModifyDateEndWith(),
                        MODIFY_DATE));
            }
            if (filter.getTireStatus() != null) {
                specification = specification.and(hasTireStatus(filter.getTireStatus()));
            }
            if (filter.getProductionYearStartsWith() != null) {
                specification = specification.and(hasIntegerTypeStartWith(filter.getProductionYearStartsWith(),
                        PRODUCTION_YEAR));
            }
            if (filter.getProductionYearEndWith() != null) {
                specification = specification.and(hasIntegerTypeEndWith(filter.getProductionYearEndWith(),
                        PRODUCTION_YEAR));
            }
            if (filter.getPurchaseDateStartsWith() != null) {
                specification = specification.and(hasDateStartWith(filter.getPurchaseDateStartsWith()));
            }
            if (filter.getPurchaseDateEndWith() != null) {
                specification = specification.and(hasDateEndWith(filter.getPurchaseDateEndWith()));
            }
            if (filter.getVehicleId() != null) {
                specification = specification.and(hasVehicleId(filter.getVehicleId()));
            }
        }
        return specification;
    }

    private static Specification<Tire> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<Tire> hasString(String text, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(fieldType), "%" + text + "%");
    }

    private static Specification<Tire> hasInstantDateStartWith(Instant date, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldType),
                date);
    }

    private static Specification<Tire> hasInstantDateEndWith(Instant date, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldType),
                date);
    }

    private static Specification<Tire> hasDateStartWith(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(TireSpecification.PURCHASE_DATE),
                date);
    }

    private static Specification<Tire> hasDateEndWith(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(TireSpecification.PURCHASE_DATE),
                date);
    }

    private static Specification<Tire> hasIntegerTypeStartWith(Integer dimension, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldType), dimension);
    }

    private static Specification<Tire> hasIntegerTypeEndWith(Integer dimension, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldType), dimension);
    }

    private static Specification<Tire> hasTireType(TireType tireType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TYPE), tireType);
    }

    private static Specification<Tire> hasReinforcedIndex(TireReinforcedIndex tireReinforcedIndex) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TIRE_REINFORCED_INDEX), tireReinforcedIndex);
    }

    private static Specification<Tire> hasSpeedIndex(TireSpeedIndex tireSpeedIndex) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SPEED_INDEX), tireSpeedIndex);
    }

    private static Specification<Tire> hasLoadCapacityIndex(TireLoadCapacityIndex tireLoadCapacityIndex) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(CAPACITY_INDEX), tireLoadCapacityIndex);
    }

    private static Specification<Tire> hasSeasonType(TireSeasonType tireSeasonType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TIRE_SEASON_TYPE), tireSeasonType);
    }

    private static Specification<Tire> hasRunOnFlat(Boolean runOnFlat) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(RUN_ON_FLAT), runOnFlat);
    }

    private static Specification<Tire> hasTireStatus(TireStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TIRE_STATUS), status);
    }

    private static Specification<Tire> hasVehicleId(Long vehicleId) {
        return (root,query, criteriaBuilder) -> {
            Join<Vehicle, Tire> vehicleTireJoin = root.join(VEHICLE);
            return criteriaBuilder.equal(vehicleTireJoin.get(VEHICLE_ID), vehicleId);
        };
    }
}
