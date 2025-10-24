package pl.com.chrzanowski.sma.company.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.company.model.Company;

import static pl.com.chrzanowski.sma.company.model.QCompany.company;

@Component
public class CompanyQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public CompanyQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(CompanyFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(company.id.eq(filter.getId()));
            }
            if (filter.getNameStartsWith() != null) {
                predicate.and(company.name.containsIgnoreCase(filter.getNameStartsWith()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Company> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Company> query = queryFactory
                .selectFrom(company)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                if ("name".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? company.name.asc() : company.name.desc());
                } else if ("id".equals(order.getProperty())) {
                    query.orderBy(order.isAscending() ? company.id.asc() : company.id.desc());
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(company.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(company.id.asc());
        }

        return query;
    }
}
