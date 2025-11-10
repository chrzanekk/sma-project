package pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;

import static pl.com.chrzanowski.sma.scaffolding.workingtime.model.QScaffoldingLogPositionWorkingTime.scaffoldingLogPositionWorkingTime;

@Component
public class ScaffoldingLogPositionWorkingTimeQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ScaffoldingLogPositionWorkingTimeQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ScaffoldingLogPositionWorkingTimeFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(scaffoldingLogPositionWorkingTime.id.eq(filter.getId()));
            }
            if (filter.getNumberOfHoursGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPositionWorkingTime.numberOfHours.goe(filter.getNumberOfHoursLesserOrEqual()));
            }
            if (filter.getNumberOfHoursLesserOrEqual() != null) {
                predicate.and(scaffoldingLogPositionWorkingTime.numberOfHours.loe(filter.getNumberOfHoursLesserOrEqual()));
            }
            if (filter.getNumberOfWorkersGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPositionWorkingTime.numberOfWorkers.goe(filter.getNumberOfWorkersGreaterOrEqual()));
            }
            if (filter.getNumberOfWorkersLesserOrEqual() != null) {
                predicate.and(scaffoldingLogPositionWorkingTime.numberOfWorkers.loe(filter.getNumberOfWorkersLesserOrEqual()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> query = queryFactory
                .selectFrom(scaffoldingLogPositionWorkingTime)
                .where(builder);

        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                if ("id".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? scaffoldingLogPositionWorkingTime.id.asc() : scaffoldingLogPositionWorkingTime.id.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(scaffoldingLogPositionWorkingTime.id.asc());
            }
        } else {
            query.orderBy(scaffoldingLogPositionWorkingTime.id.asc());
        }
        return query;
    }
}
