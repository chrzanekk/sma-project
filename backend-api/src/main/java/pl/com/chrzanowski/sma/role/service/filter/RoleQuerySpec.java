package pl.com.chrzanowski.sma.role.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.role.model.QRole;
import pl.com.chrzanowski.sma.role.model.Role;

@Component
public class RoleQuerySpec {
    private final BlazeJPAQueryFactory queryFactory;

    public RoleQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
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

    public BlazeJPAQuery<Role> buildQuery(BooleanBuilder builder, Pageable pageable) {
        return QueryBuilderUtil.buildQuery(queryFactory, Role.class, "role", builder, pageable, QRole.role.id.asc());
    }
}
