package pl.com.chrzanowski.sma.scaffolding.log.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;

import static pl.com.chrzanowski.sma.scaffolding.log.model.QScaffoldingLog.scaffoldingLog;

@Component
public class ScaffoldingLogQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ScaffoldingLogQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    public static BooleanBuilder buildPredicate(ScaffoldingLogFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(scaffoldingLog.id.eq(filter.getId()));
            }
            if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                predicate.and(scaffoldingLog.name.containsIgnoreCase(filter.getNameContains()));
            }
            if (filter.getAdditionalInfoContains() != null && !filter.getAdditionalInfoContains().isEmpty()) {
                predicate.and(scaffoldingLog.additionalInfo.containsIgnoreCase(filter.getAdditionalInfoContains()));
            }
            if (filter.getContractorId() != null) {
                predicate.and(scaffoldingLog.contractor.id.eq(filter.getContractorId()));
            }
            if (filter.getConstructionSiteId() != null) {
                predicate.and(scaffoldingLog.constructionSite.id.eq(filter.getConstructionSiteId()));
            }
            if (filter.getCompanyId() != null) {
                predicate.and(scaffoldingLog.company.id.eq(filter.getCompanyId()));
            }
            if (filter.getConstructionSiteNameContains() != null && !filter.getConstructionSiteNameContains().isEmpty()) {
                predicate.and(scaffoldingLog.constructionSite.name.containsIgnoreCase(filter.getConstructionSiteNameContains()));
            }
            if (filter.getContractorNameContains() != null && !filter.getContractorNameContains().isEmpty()) {
                predicate.and(scaffoldingLog.contractor.name.containsIgnoreCase(filter.getContractorNameContains()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<ScaffoldingLog> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<ScaffoldingLog> query = queryFactory
                .selectFrom(scaffoldingLog)
                .where(builder);
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "id":
                        query.orderBy(order.isAscending() ? scaffoldingLog.id.asc() : scaffoldingLog.id.desc());
                        break;
                    case "name":
                        query.orderBy(order.isAscending() ? scaffoldingLog.name.asc() : scaffoldingLog.name.desc());
                        break;
                    case "additionalInfo":
                        query.orderBy(order.isAscending() ? scaffoldingLog.additionalInfo.asc() : scaffoldingLog.additionalInfo.desc());
                        break;
                    case "constructionSiteName":
                        query.orderBy(order.isAscending() ? scaffoldingLog.constructionSite.name.asc() : scaffoldingLog.constructionSite.name.desc());
                        break;
                    case "contractorName":
                        query.orderBy(order.isAscending() ? scaffoldingLog.contractor.name.asc() : scaffoldingLog.contractor.name.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(scaffoldingLog.id.asc());
            }
        } else {
            query.orderBy(scaffoldingLog.id.asc());
        }
        return query;
    }
}
