package pl.com.chrzanowski.sma.user.service.filter;

import com.querydsl.core.BooleanBuilder;
import pl.com.chrzanowski.sma.user.model.QUser;


public class UserQuerySpec {
    public static BooleanBuilder buildPredicate(UserFilter userFilter) {
        QUser user = QUser.user;
        BooleanBuilder predicate = new BooleanBuilder();

        if (userFilter != null) {
            if (userFilter.getId() != null) {
                predicate.and(user.id.eq(userFilter.getId()));
            }
            if (userFilter.getUsernameStartsWith() != null) {
                predicate.and(user.username.startsWithIgnoreCase(userFilter.getUsernameStartsWith()));
            }
            if (userFilter.getEmailStartsWith() != null) {
                predicate.and(user.email.startsWithIgnoreCase(userFilter.getEmailStartsWith()));
            }
            if (userFilter.getIsEnabled() != null) {
                predicate.and(user.enabled.eq(userFilter.getIsEnabled()));
            }
            if (userFilter.getIsLocked() != null) {
                predicate.and(user.locked.eq(userFilter.getIsLocked()));
            }
        }
        return predicate;
    }
}
