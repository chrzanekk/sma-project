package pl.com.chrzanowski.sma.contractor.service.filter;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.contractor.model.QContractor;

@Component
public class ContractorQuerySpec {

    public static BooleanBuilder buildPredicate(ContractorFilter filter) {
        QContractor contractor = QContractor.contractor;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getId() != null) {
            builder.and(contractor.id.eq(filter.getId()));
        }
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            builder.and(contractor.name.containsIgnoreCase(filter.getName()));
        }
        if (filter.getTaxNumber() != null && !filter.getTaxNumber().isEmpty()) {
            builder.and(contractor.taxNumber.containsIgnoreCase(filter.getTaxNumber()));
        }
        if (filter.getStreet() != null && !filter.getStreet().isEmpty()) {
            builder.and(contractor.street.containsIgnoreCase(filter.getStreet()));
        }
        if (filter.getBuildingNo() != null && !filter.getBuildingNo().isEmpty()) {
            builder.and(contractor.buildingNo.containsIgnoreCase(filter.getBuildingNo()));
        }
        if (filter.getApartmentNo() != null && !filter.getApartmentNo().isEmpty()) {
            builder.and(contractor.apartmentNo.containsIgnoreCase(filter.getApartmentNo()));
        }
        if (filter.getPostalCode() != null && !filter.getPostalCode().isEmpty()) {
            builder.and(contractor.postalCode.containsIgnoreCase(filter.getPostalCode()));
        }
        if (filter.getCity() != null && !filter.getCity().isEmpty()) {
            builder.and(contractor.city.containsIgnoreCase(filter.getCity()));
        }
        if (filter.getCountry() != null) {
            builder.and(contractor.country.eq(filter.getCountry()));
        }
        if (filter.getCreateDateStartWith() != null) {
            builder.and(contractor.createdDatetime.goe(filter.getCreateDateStartWith()));
        }
        if (filter.getCreateDateEndWith() != null) {
            builder.and(contractor.createdDatetime.loe(filter.getCreateDateEndWith()));
        }
        if (filter.getModifyDateStartWith() != null) {
            builder.and(contractor.lastModifiedDatetime.goe(filter.getModifyDateStartWith()));
        }
        if (filter.getModifyDateEndWith() != null) {
            builder.and(contractor.lastModifiedDatetime.loe(filter.getModifyDateEndWith()));
        }

        return builder;
    }
}
