package pl.com.chrzanowski.sma.user.service.filter;

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
import pl.com.chrzanowski.sma.user.model.QUser;
import pl.com.chrzanowski.sma.user.model.User;

@Component
public class UserQuerySpec {

    private final EntityManager em;

    public UserQuerySpec(EntityManager em) {
        this.em = em;
    }

    public static BooleanBuilder buildPredicate(UserFilter userFilter) {
        QUser user = QUser.user;
        BooleanBuilder predicate = new BooleanBuilder();

        if (userFilter != null) {
            if (userFilter.getId() != null) {
                predicate.and(user.id.eq(userFilter.getId()));
            }
            if (userFilter.getLoginStartsWith() != null && !userFilter.getLoginStartsWith().isEmpty()) {
                predicate.and(user.login.startsWithIgnoreCase(userFilter.getLoginStartsWith()));
            }
            if (userFilter.getEmailStartsWith() != null && !userFilter.getEmailStartsWith().isEmpty()) {
                predicate.and(user.email.startsWithIgnoreCase(userFilter.getEmailStartsWith()));
            }
            if (userFilter.getFirstNameStartsWith() != null && !userFilter.getFirstNameStartsWith().isEmpty()) {
                predicate.and(user.firstName.startsWithIgnoreCase(userFilter.getFirstNameStartsWith()));
            }
            if (userFilter.getLastNameStartsWith() != null && !userFilter.getLastNameStartsWith().isEmpty()) {
                predicate.and(user.lastName.startsWithIgnoreCase(userFilter.getLastNameStartsWith()));
            }
            if (userFilter.getPositionStartsWith() != null && !userFilter.getPositionStartsWith().isEmpty()) {
                predicate.and(user.position.startsWithIgnoreCase(userFilter.getPositionStartsWith()));
            }
            if (userFilter.getIsEnabled() != null) {
                predicate.and(user.enabled.eq(userFilter.getIsEnabled()));
            }
            if (userFilter.getIsLocked() != null) {
                predicate.and(user.locked.eq(userFilter.getIsLocked()));
            }
            if (userFilter.getRoles() != null && !userFilter.getRoles().isEmpty()) {
                predicate.and(user.roles.any().name.in(userFilter.getRoles()));
            }
        }
        return predicate;
    }

    public JPQLQuery<User> buildQuery(BooleanBuilder booleanBuilder, Pageable pageable) {
        QUser user = QUser.user;

        JPQLQuery<User> query = new JPAQuery<>(em).select(user).distinct().from(user);

        if (booleanBuilder != null) {
            query.where(booleanBuilder);
        }

        if (pageable != null && pageable.getSort().isSorted()) {
            PathBuilder<User> contractorPathBuilder = new PathBuilder<>(User.class, "user");
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        contractorPathBuilder.getComparable(order.getProperty(), String.class)
                );
                query.orderBy(orderSpecifier);
            }
        }

        return query;
    }
}
