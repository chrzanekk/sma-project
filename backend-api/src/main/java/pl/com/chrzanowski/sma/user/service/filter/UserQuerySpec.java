package pl.com.chrzanowski.sma.user.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
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
        QRole role = QRole.role;
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
            if(userFilter.getRoles() != null && !userFilter.getRoles().isEmpty()) {
                predicate.and(user.roles.any().name.in(userFilter.getRoles()));
            }
        }
        return predicate;
    }

    public JPQLQuery<User> buildQuery(BooleanBuilder booleanBuilder) {
        QUser user = QUser.user;

        JPQLQuery<User> query = new JPAQuery<>(em).select(user).from(user);

        if(booleanBuilder != null) {
            query.where(booleanBuilder);
        }
        query.leftJoin(user.roles).fetchJoin();
        return query;
    }
}
