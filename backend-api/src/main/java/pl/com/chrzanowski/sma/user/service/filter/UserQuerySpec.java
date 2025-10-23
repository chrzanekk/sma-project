package pl.com.chrzanowski.sma.user.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.user.model.User;

import static pl.com.chrzanowski.sma.user.model.QUser.user;

@Component
public class UserQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public UserQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(UserFilter userFilter) {

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
                predicate.and(user.position.name.startsWithIgnoreCase(userFilter.getPositionStartsWith()));
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

    public BlazeJPAQuery<User> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<User> query = queryFactory
                .selectFrom(user)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "login":
                        query.orderBy(order.isAscending() ? user.login.asc() : user.login.desc());
                        break;
                    case "email":
                        query.orderBy(order.isAscending() ? user.email.asc() : user.email.desc());
                        break;
                    case "firstName":
                        query.orderBy(order.isAscending() ? user.firstName.asc() : user.firstName.desc());
                        break;
                    case "lastName":
                        query.orderBy(order.isAscending() ? user.lastName.asc() : user.lastName.desc());
                        break;
                    case "position":
                        query.orderBy(order.isAscending() ? user.position.name.asc() : user.position.name.desc());
                        break;
                    case "enabled":
                        query.orderBy(order.isAscending() ? user.enabled.asc() : user.enabled.desc());
                        break;
                    case "locked":
                        query.orderBy(order.isAscending() ? user.locked.asc() : user.locked.desc());
                        break;
                    case "id":
                        query.orderBy(order.isAscending() ? user.id.asc() : user.id.desc());
                        break;
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(user.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(user.id.asc());
        }

        return query;
    }
}
