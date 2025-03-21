package pl.com.chrzanowski.sma.company.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
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

    public JPQLQuery<Company> buildQuery(BooleanBuilder booleanBuilder) {
        QCompany company = QCompany.company;
        return new JPAQuery<Company>(em).select(company).from(company).where(booleanBuilder);
    }
}
