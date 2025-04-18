package pl.com.chrzanowski.sma.constructionsite.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite;

@Component
public class ConstructionSiteQuerySpec {

    private final EntityManager em;

    public ConstructionSiteQuerySpec(EntityManager em) {
        this.em = em;
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
        if (filter.getShortNameStartsWith() != null && !filter.getShortNameStartsWith().isEmpty()) {
            builder.and(constructionSite.shortName.containsIgnoreCase(filter.getShortNameStartsWith()));
        }
        if (filter.getCodeStartsWith() != null && !filter.getCodeStartsWith().isEmpty()) {
            builder.and(constructionSite.code.containsIgnoreCase(filter.getCodeStartsWith()));
        }
        if (filter.getCountry() != null) {
            builder.and(constructionSite.country.eq(filter.getCountry()));
        }
        if (filter.getCompanyId() != null) {
            builder.and(constructionSite.company.id.eq(filter.getCompanyId()));
        }
        if (filter.getContractorNameStartsWith() != null && !filter.getContractorNameStartsWith().isEmpty()) {
            builder.and(constructionSite.siteContractors.any().contractor.name.containsIgnoreCase(filter.getContractorNameStartsWith()));
        }
        return builder;
    }

    public JPQLQuery<ConstructionSite> buildQuery(BooleanBuilder builder, Pageable pageable) {
        QConstructionSite constructionSite = QConstructionSite.constructionSite;
        JPQLQuery<ConstructionSite> query = new JPAQuery<>(em).select(constructionSite).distinct().from(constructionSite);

        if (builder != null) {
            query.where(builder);
        }

        if (pageable != null && pageable.getSort().isSorted()) {
            PathBuilder<ConstructionSite> constructionSitePathBuilder = new PathBuilder<>(ConstructionSite.class, "constructionSite");
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        constructionSitePathBuilder.getComparable(order.getProperty(), String.class)
                );
                query.orderBy(orderSpecifier);
            }
        }

        return query;
    }
}
