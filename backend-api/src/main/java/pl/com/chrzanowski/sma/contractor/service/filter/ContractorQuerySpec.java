package pl.com.chrzanowski.sma.contractor.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import static pl.com.chrzanowski.sma.contractor.model.QContractor.contractor;

@Component
public class ContractorQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ContractorQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ContractorFilter filter) {
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
        BlazeJPAQuery<Contractor> query = queryFactory
                .selectFrom(contractor)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "name":
                        query.orderBy(order.isAscending() ? contractor.name.asc() : contractor.name.desc());
                        break;
                    case "taxNumber":
                        query.orderBy(order.isAscending() ? contractor.taxNumber.asc() : contractor.taxNumber.desc());
                        break;
                    case "street":
                        query.orderBy(order.isAscending() ? contractor.street.asc() : contractor.street.desc());
                        break;
                    case "city":
                        query.orderBy(order.isAscending() ? contractor.city.asc() : contractor.city.desc());
                        break;
                    case "postalCode":
                        query.orderBy(order.isAscending() ? contractor.postalCode.asc() : contractor.postalCode.desc());
                        break;
                    case "country":
                        query.orderBy(order.isAscending() ? contractor.country.asc() : contractor.country.desc());
                        break;
                    case "customer":
                        query.orderBy(order.isAscending() ? contractor.customer.asc() : contractor.customer.desc());
                        break;
                    case "supplier":
                        query.orderBy(order.isAscending() ? contractor.supplier.asc() : contractor.supplier.desc());
                        break;
                    case "scaffoldingUser":
                        query.orderBy(order.isAscending() ? contractor.scaffoldingUser.asc() : contractor.scaffoldingUser.desc());
                        break;
                    case "createdDatetime":
                        query.orderBy(order.isAscending() ? contractor.createdDatetime.asc() : contractor.createdDatetime.desc());
                        break;
                    case "lastModifiedDatetime":
                        query.orderBy(order.isAscending() ? contractor.lastModifiedDatetime.asc() : contractor.lastModifiedDatetime.desc());
                        break;
                    case "id":
                        query.orderBy(order.isAscending() ? contractor.id.asc() : contractor.id.desc());
                        break;
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(contractor.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(contractor.id.asc());
        }

        return query;
    }
}
