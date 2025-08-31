package pl.com.chrzanowski.sma.user.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.user.model.QUser;
import pl.com.chrzanowski.sma.user.model.User;

@Component
public class UserQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public UserQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
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

    public BlazeJPAQuery<User> buildQuery(BooleanBuilder booleanBuilder, Pageable pageable) {
        return QueryBuilderUtil.buildQuery(queryFactory, User.class, "user", booleanBuilder, pageable, QUser.user.id.asc());
    }
}
