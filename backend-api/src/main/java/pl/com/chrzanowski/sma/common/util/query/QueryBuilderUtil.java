package pl.com.chrzanowski.sma.common.util.query;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class QueryBuilderUtil {
    public static <T> BlazeJPAQuery<T> buildQuery(
            BlazeJPAQueryFactory queryFactory,
            Class<T> entityClass,
            String alias,
            BooleanBuilder builder,
            Pageable pageable,
            OrderSpecifier<?> defaultSort
    ) {
        PathBuilder<T> path = new PathBuilder<>(entityClass, alias);
        BlazeJPAQuery<T> query = queryFactory.selectFrom(path);

        if (builder != null) {
            query.where(builder);
        }

        if (pageable != null && pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                query.orderBy(new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        path.getComparable(order.getProperty(), String.class)
                ));
            }
        }

        if (query.getMetadata().getOrderBy().isEmpty()) {
            query.orderBy(defaultSort);
        }

        return query;
    }
}
