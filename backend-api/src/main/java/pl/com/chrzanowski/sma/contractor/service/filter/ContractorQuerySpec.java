package pl.com.chrzanowski.sma.contractor.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.model.QContractor;

@Component
public class ContractorQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ContractorQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ContractorFilter filter) {
        QContractor contractor = QContractor.contractor;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getId() != null) {
            builder.and(contractor.id.eq(filter.getId()));
        }
        if (filter.getNameStartsWith() != null && !filter.getNameStartsWith().isEmpty()) {
            builder.and(contractor.name.containsIgnoreCase(filter.getNameStartsWith()));
        }
        if (filter.getTaxNumberStartsWith() != null && !filter.getTaxNumberStartsWith().isEmpty()) {
            builder.and(contractor.taxNumber.containsIgnoreCase(filter.getTaxNumberStartsWith()));
        }
        if (filter.getStreetStartsWith() != null && !filter.getStreetStartsWith().isEmpty()) {
            builder.and(contractor.street.containsIgnoreCase(filter.getStreetStartsWith()));
        }
        if (filter.getBuildingNoStartsWith() != null && !filter.getBuildingNoStartsWith().isEmpty()) {
            builder.and(contractor.buildingNo.containsIgnoreCase(filter.getBuildingNoStartsWith()));
        }
        if (filter.getApartmentNoStartsWith() != null && !filter.getApartmentNoStartsWith().isEmpty()) {
            builder.and(contractor.apartmentNo.containsIgnoreCase(filter.getApartmentNoStartsWith()));
        }
        if (filter.getPostalCodeStartsWith() != null && !filter.getPostalCodeStartsWith().isEmpty()) {
            builder.and(contractor.postalCode.containsIgnoreCase(filter.getPostalCodeStartsWith()));
        }
        if (filter.getCityStartsWith() != null && !filter.getCityStartsWith().isEmpty()) {
            builder.and(contractor.city.containsIgnoreCase(filter.getCityStartsWith()));
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
        if (filter.getCustomer() != null) {
            builder.and(contractor.customer.eq(filter.getCustomer()));
        }
        if (filter.getSupplier() != null) {
            builder.and(contractor.supplier.eq(filter.getSupplier()));
        }
        if (filter.getScaffoldingUser() != null) {
            builder.and(contractor.scaffoldingUser.eq(filter.getScaffoldingUser()));
        }
        if (filter.getCompanyId() != null && filter.getCompanyId() > 0) {
            builder.and(contractor.company.id.eq(filter.getCompanyId()));
        }
        return builder;
    }

    public BlazeJPAQuery<Contractor> buildQuery(BooleanBuilder builder, Pageable pageable) {
        return QueryBuilderUtil.buildQuery(queryFactory, Contractor.class, "contractor", builder, pageable, QContractor.contractor.id.asc());
    }
}
