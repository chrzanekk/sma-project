package pl.com.chrzanowski.sma.position.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.position.model.Position;

import static pl.com.chrzanowski.sma.position.model.QPosition.position;

@Component
public class PositionQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public PositionQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(PositionFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(position.id.eq(filter.getId()));
            }
            if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                predicate.and(position.name.containsIgnoreCase(filter.getNameContains()));
            }
            if (filter.getDescriptionContains() != null && !filter.getDescriptionContains().isEmpty()) {
                predicate.and(position.description.containsIgnoreCase(filter.getDescriptionContains()));
            }
            if (filter.getCompanyId() != null) {
                predicate.and(position.company.id.eq(filter.getCompanyId()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Position> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Position> query = queryFactory.selectFrom(position).where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                if ("name".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? position.name.asc() : position.name.desc());
                } else if ("id".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? position.id.asc() : position.id.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(position.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(position.id.asc());
        }
        return query;
    }
}
