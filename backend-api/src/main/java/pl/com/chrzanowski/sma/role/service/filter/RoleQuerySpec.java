package pl.com.chrzanowski.sma.role.service.filter;

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
import pl.com.chrzanowski.sma.role.model.QRole;
import pl.com.chrzanowski.sma.role.model.Role;

@Component
public class RoleQuerySpec {
    private final EntityManager em;

    public RoleQuerySpec(EntityManager em) {
        this.em = em;
    }

    public static BooleanBuilder buildPredicate(RoleFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        QRole role = QRole.role;
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

    public JPQLQuery<Role> buildQuery(BooleanBuilder builder, Pageable pageable) {
        QRole role = QRole.role;

        JPQLQuery<Role> query = new JPAQuery<Role>(em).select(role).from(role);
        if (builder != null) {
            query.where(builder);
        }
        if (pageable != null && pageable.getSort().isSorted()) {
            PathBuilder<Role> companyPathBuilder = new PathBuilder<>(Role.class, "role");
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        companyPathBuilder.getComparable(order.getProperty(), String.class)
                );
                query.orderBy(orderSpecifier);
            }
        }

        return query;
    }
}
