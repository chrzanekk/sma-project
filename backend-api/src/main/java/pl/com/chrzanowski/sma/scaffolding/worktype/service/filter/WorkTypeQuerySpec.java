package pl.com.chrzanowski.sma.scaffolding.worktype.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;

import static pl.com.chrzanowski.sma.scaffolding.worktype.model.QWorkType.workType;

@Component
public class WorkTypeQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public WorkTypeQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(WorkTypeFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(workType.id.eq(filter.getId()));
            }
            if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                predicate.and(workType.name.containsIgnoreCase(filter.getNameContains()));
            }
            if (filter.getDescriptionContains() != null && !filter.getDescriptionContains().isEmpty()) {
                predicate.and(workType.description.containsIgnoreCase(filter.getDescriptionContains()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<WorkType> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<WorkType> query = queryFactory.selectFrom(workType).where(builder);

        if (pageable != null) {
            Sort sort = pageable.getSort();
            sort.forEach(s -> {
                switch (s.getProperty()) {
                    case "name":
                        query.orderBy(s.isAscending() ? workType.name.asc() : workType.name.desc());
                        break;
                    case "description":
                        query.orderBy(s.isAscending() ? workType.description.asc() : workType.description.desc());
                }
            });
            if (sort.stream().noneMatch(s -> "id".equals(s.getProperty()))) {
                query.orderBy(workType.id.asc());
            }
        } else {
            query.orderBy(workType.id.asc());
        }
        return query;
    }
}
