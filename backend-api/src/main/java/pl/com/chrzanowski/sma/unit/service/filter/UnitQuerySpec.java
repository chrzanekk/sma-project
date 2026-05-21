package pl.com.chrzanowski.sma.unit.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.enumeration.UnitType;
import pl.com.chrzanowski.sma.unit.model.Unit;

import static pl.com.chrzanowski.sma.unit.model.QUnit.unit;

@Component
public class UnitQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public UnitQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(UnitFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(unit.id.eq(filter.getId()));
            }
            if (filter.getSymbolContains() != null && !filter.getSymbolContains().isEmpty()) {
                predicate.and(unit.symbol.containsIgnoreCase(filter.getSymbolContains()));
            }
            if (filter.getDescriptionContains() != null && !filter.getDescriptionContains().isEmpty()) {
                predicate.and(unit.description.containsIgnoreCase(filter.getDescriptionContains()));
            }
            if (filter.getCompanyId() != null) {
                predicate.and(unit.company.id.eq(filter.getCompanyId()));
            }
            if(filter.getUnitType() != null &&  !filter.getUnitType().isEmpty()) {
                UnitType unitType = UnitType.valueOf(filter.getUnitType());
                predicate.and(unit.unitType.eq(unitType));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Unit> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Unit> query = queryFactory.selectFrom(unit).where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                if ("name".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? unit.symbol.asc() : unit.symbol.desc());
                } else if ("id".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? unit.id.asc() : unit.id.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(unit.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(unit.id.asc());
        }
        return query;
    }
}
