package pl.com.chrzanowski.sma.role.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
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
        if(filter != null) {
            if(filter.getId() != null) {
                predicate.and(role.id.eq(filter.getId()));
            }
            if(filter.getName() != null) {
                predicate.and(role.name.containsIgnoreCase(filter.getName()));
            }
        }
        return predicate;
    }

    public JPQLQuery<Role> buildQuery(BooleanBuilder builder) {
        QRole role = QRole.role;
        return new JPAQuery<Role>(em).select(role).from(role);
    }
}
