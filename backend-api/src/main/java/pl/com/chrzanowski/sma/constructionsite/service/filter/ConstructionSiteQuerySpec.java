package pl.com.chrzanowski.sma.constructionsite.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite;

@Component
public class ConstructionSiteQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ConstructionSiteQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ConstructionSiteFilter filter) {
        QConstructionSite constructionSite = QConstructionSite.constructionSite;
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
        return QueryBuilderUtil.buildQuery(queryFactory, ConstructionSite.class, "constructionSite", builder, pageable, QConstructionSite.constructionSite.id.asc());
    }
}
