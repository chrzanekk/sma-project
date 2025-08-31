package pl.com.chrzanowski.sma.company.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.model.QCompany;

@Component
public class CompanyQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public CompanyQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(CompanyFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        QCompany company = QCompany.company;
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

    public BlazeJPAQuery<Company> buildQuery(BooleanBuilder booleanBuilder, Pageable pageable) {
        return QueryBuilderUtil.buildQuery(queryFactory, Company.class, "company", booleanBuilder, pageable, QCompany.company.id.asc());
    }
}
