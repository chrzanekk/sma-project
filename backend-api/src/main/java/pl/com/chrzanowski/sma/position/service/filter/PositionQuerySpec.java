package pl.com.chrzanowski.sma.position.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.model.QPosition;

@Component
public class PositionQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public PositionQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(PositionFilter filter) {
        QPosition position = QPosition.position;
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(position.id.eq(filter.getId()));
            }
            if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                predicate.and(position.name.containsIgnoreCase(filter.getNameContains()));
            }
            if (filter.getDescriptionContains() != null && !filter.getDescriptionContains().isEmpty()) {
                predicate.and(position.description.containsIgnoreCase(filter.getDescriptionContains()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Position> buildQuery(BooleanBuilder builder, Pageable pageable) {
        return QueryBuilderUtil.buildQuery(queryFactory, Position.class, "position", builder, pageable, QPosition.position.id.asc());
    }
}
