package pl.com.chrzanowski.sma.company.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.model.QCompany;

@Component
public class CompanyQuerySpec {
    private final EntityManager em;

    public CompanyQuerySpec(EntityManager em) {
        this.em = em;
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

    public JPQLQuery<Company> buildQuery(BooleanBuilder booleanBuilder, Pageable pageable) {
        QCompany company = QCompany.company;

        JPQLQuery<Company> query = new JPAQuery<>(em).select(company).from(company);

        if (booleanBuilder != null) {
            query.where(booleanBuilder);
        }
        if (pageable != null && pageable.getSort().isSorted()) {
            PathBuilder<Company> companyPathBuilder = new PathBuilder<>(Company.class, "company");
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        companyPathBuilder.getComparable(order.getProperty(), String.class)
                );
                query.orderBy(orderSpecifier);
            }
        }

        return query;
    }
}
