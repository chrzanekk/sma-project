package pl.com.chrzanowski.sma.role.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.role.model.Role;

import static pl.com.chrzanowski.sma.role.model.QRole.role;

@Component
public class RoleQuerySpec {
    private final BlazeJPAQueryFactory queryFactory;

    public RoleQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(RoleFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(role.id.eq(filter.getId()));
            }
            if (filter.getName() != null) {
                predicate.and(role.name.containsIgnoreCase(filter.getName()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Role> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Role> query = queryFactory
                .selectFrom(role)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                if ("name".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? role.name.asc() : role.name.desc());
                } else if ("id".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? role.id.asc() : role.id.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(role.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(role.id.asc());
        }

        return query;
    }
}
