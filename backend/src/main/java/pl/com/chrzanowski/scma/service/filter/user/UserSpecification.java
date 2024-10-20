package pl.com.chrzanowski.scma.service.filter.user;

import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.scma.domain.User;

public class UserSpecification {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String USER_NAME = "userName";
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
            if (filter.isEnabled() != null) {
                specification = specification.and(hasBoolean(filter.isEnabled(), IS_ENABLED));
            }
            if (filter.isLocked() != null) {
                specification = specification.and(hasBoolean(filter.isLocked(), IS_LOCKED));
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
