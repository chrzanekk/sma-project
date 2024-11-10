package pl.com.chrzanowski.sma.user.service.filter;

import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.user.model.User;

public class UserSpecification {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String USER_NAME = "username";
    private static final String IS_LOCKED = "isLocked";
    private static final String IS_ENABLED = "isEnabled";

    public static Specification<User> create(UserFilter filter) {
        Specification<User> specification = Specification.where(null);
        if (filter != null) {
            if (filter.getId() != null) {
                specification = specification.and(hasId(filter.getId()));
            }
            if (filter.getUsernameStartsWith() != null) {
                specification = specification.and(hasString(filter.getUsernameStartsWith(), USER_NAME));
            }
            if (filter.getEmailStartsWith() != null) {
                specification = specification.and(hasString(filter.getEmailStartsWith(), EMAIL));
            }
            if (filter.getIsEnabled() != null) {
                specification = specification.and(hasBoolean(filter.getIsEnabled(), IS_ENABLED));
            }
            if (filter.getIsLocked() != null) {
                specification = specification.and(hasBoolean(filter.getIsEnabled(), IS_LOCKED));
            }
        }
        return specification;
    }

    private static Specification<User> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Long>get(ID), id);
    }

    private static Specification<User> hasString(String text, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(fieldType), "%" + text + "%");
    }

    private static Specification<User> hasBoolean(Boolean runOnFlat, String fieldType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldType), runOnFlat);
    }

}
