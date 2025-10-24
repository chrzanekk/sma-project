package pl.com.chrzanowski.sma.constructionsite.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;

import static pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite.constructionSite;

@Component
public class ConstructionSiteQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ConstructionSiteQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ConstructionSiteFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getId() != null) {
            builder.and(constructionSite.id.eq(filter.getId()));
        }
        if (filter.getNameStartsWith() != null && !filter.getNameStartsWith().isEmpty()) {
            builder.and(constructionSite.name.containsIgnoreCase(filter.getNameStartsWith()));
        }
        if (filter.getAddressStartsWith() != null && !filter.getAddressStartsWith().isEmpty()) {
            builder.and(constructionSite.address.containsIgnoreCase(filter.getAddressStartsWith()));
        }
        if (filter.getShortNameStartsWith() != null && !filter.getShortNameStartsWith().isEmpty()) {
            builder.and(constructionSite.shortName.containsIgnoreCase(filter.getShortNameStartsWith()));
        }
        if (filter.getCodeStartsWith() != null && !filter.getCodeStartsWith().isEmpty()) {
            builder.and(constructionSite.code.containsIgnoreCase(filter.getCodeStartsWith()));
        }
        if (filter.getCountryCode() != null && !filter.getCountryCode().isBlank()) {
            Country enumCountry = Country.fromCode(filter.getCountryCode());
                builder.and(constructionSite.country.eq(enumCountry));
        }
        if (filter.getCompanyId() != null) {
            builder.and(constructionSite.company.id.eq(filter.getCompanyId()));
        }
        if (filter.getContractorNameStartsWith() != null && !filter.getContractorNameStartsWith().isEmpty()) {
            builder.and(constructionSite.siteContractors.any().contractor.name.containsIgnoreCase(filter.getContractorNameStartsWith()));
        }
        return builder;
    }

    public BlazeJPAQuery<ConstructionSite> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<ConstructionSite> query = queryFactory
                .selectFrom(constructionSite)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "name":
                        query.orderBy(order.isAscending() ? constructionSite.name.asc() : constructionSite.name.desc());
                        break;
                    case "code":
                        query.orderBy(order.isAscending() ? constructionSite.code.asc() : constructionSite.code.desc());
                        break;
                    case "shortName":
                        query.orderBy(order.isAscending() ? constructionSite.shortName.asc() : constructionSite.shortName.desc());
                        break;
                    case "address":
                        query.orderBy(order.isAscending() ? constructionSite.address.asc() : constructionSite.address.desc());
                        break;
                    case "country":
                        query.orderBy(order.isAscending() ? constructionSite.country.asc() : constructionSite.country.desc());
                        break;
                    case "id":
                        query.orderBy(order.isAscending() ? constructionSite.id.asc() : constructionSite.id.desc());
                        break;
                }
            });

            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(constructionSite.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(constructionSite.id.asc());
        }

        return query;
    }
}
