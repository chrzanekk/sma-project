package pl.com.chrzanowski.sma.scaffolding.dimension.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;

import static pl.com.chrzanowski.sma.scaffolding.dimension.model.QScaffoldingLogPositionDimension.scaffoldingLogPositionDimension;

@Component
public class ScaffoldingLogPositionDimensionQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ScaffoldingLogPositionDimensionQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ScaffoldingLogPositionDimensionFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(scaffoldingLogPositionDimension.id.eq(filter.getId()));
            }
            if (filter.getScaffoldingPositionId() != null) {
                predicate.and(scaffoldingLogPositionDimension.scaffoldingPosition.id.eq(filter.getScaffoldingPositionId()));
            }
            if (filter.getDimensionType() != null) {
                predicate.and(scaffoldingLogPositionDimension.dimensionType.eq(filter.getDimensionType()));
            }
            if (filter.getDismantlingDateLesserOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.dismantlingDate.loe(filter.getDismantlingDateLesserOrEqual()));
            }
            if (filter.getDismantlingDateGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.dismantlingDate.goe(filter.getDismantlingDateGreaterOrEqual()));
            }
            if (filter.getHeightGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.height.goe(filter.getHeightGreaterOrEqual()));
            }
            if (filter.getHeightLesserOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.height.loe(filter.getHeightLesserOrEqual()));
            }
            if (filter.getWidthGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.width.goe(filter.getWidthGreaterOrEqual()));
            }
            if (filter.getWidthLesserOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.width.loe(filter.getWidthLesserOrEqual()));
            }
            if (filter.getLengthGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.length.goe(filter.getLengthGreaterOrEqual()));
            }
            if (filter.getLengthLesserOrEqual() != null) {
                predicate.and(scaffoldingLogPositionDimension.width.loe(filter.getLengthLesserOrEqual()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<ScaffoldingLogPositionDimension> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<ScaffoldingLogPositionDimension> query = queryFactory
                .selectFrom(scaffoldingLogPositionDimension)
                .where(builder);

        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "id":
                        query.orderBy(order.isAscending() ? scaffoldingLogPositionDimension.id.asc() : scaffoldingLogPositionDimension.id.desc());
                        break;
                    case "scaffoldingPositionId":
                        query.orderBy(order.isAscending() ? scaffoldingLogPositionDimension.scaffoldingPosition.id.asc() : scaffoldingLogPositionDimension.scaffoldingPosition.id.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(scaffoldingLogPositionDimension.id.asc());
            }
        } else {
            query.orderBy(scaffoldingLogPositionDimension.id.asc());
        }
        return query;
    }
}
