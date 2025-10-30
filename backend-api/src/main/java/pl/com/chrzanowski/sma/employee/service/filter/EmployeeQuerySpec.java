package pl.com.chrzanowski.sma.employee.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.employee.model.Employee;

import static pl.com.chrzanowski.sma.employee.model.QEmployee.employee;

@Component
public class EmployeeQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public EmployeeQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(EmployeeFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(employee.id.eq(filter.getId()));
            }
            if (filter.getCompanyId() != null) {
                predicate.and(employee.company.id.eq(filter.getCompanyId()));
            }
            if (filter.getPositionId() != null) {
                predicate.and(employee.position.id.eq(filter.getPositionId()));
            }
            if (filter.getFirstNameContains() != null && !filter.getFirstNameContains().isEmpty()) {
                predicate.and(employee.firstName.containsIgnoreCase(filter.getFirstNameContains()));
            }
            if (filter.getLastNameContains() != null && !filter.getLastNameContains().isEmpty()) {
                predicate.and(employee.lastName.containsIgnoreCase(filter.getLastNameContains()));
            }
            if (filter.getCompanyContains() != null && !filter.getCompanyContains().isEmpty()) {
                predicate.and(employee.company.name.containsIgnoreCase(filter.getCompanyContains()));
            }
            if (filter.getPositionContains() != null && !filter.getPositionContains().isEmpty()) {
                predicate.and(employee.position.name.containsIgnoreCase(filter.getPositionContains()));
            }
            if (filter.getHourRateStartsWith() != null) {
                predicate.and(employee.hourRate.goe(filter.getHourRateStartsWith()));
            }
            if (filter.getHourRateEndsWith() != null) {
                predicate.and(employee.hourRate.loe(filter.getHourRateEndsWith()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Employee> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Employee> query = queryFactory
                .selectFrom(employee)
                .where(builder);

        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "id":
                        query.orderBy(order.isAscending() ? employee.id.asc() : employee.id.desc());
                        break;
                    case "firstName":
                        query.orderBy(order.isAscending() ? employee.firstName.asc() : employee.firstName.desc());
                        break;
                    case "lastName":
                        query.orderBy(order.isAscending() ? employee.lastName.asc() : employee.lastName.desc());
                        break;
                    case "hourRate":
                        query.orderBy(order.isAscending() ? employee.hourRate.asc() : employee.hourRate.desc());
                        break;
                }
            });
            if (sort.stream().noneMatch(order -> "id".equalsIgnoreCase(order.getProperty()))) {
                query.orderBy(employee.id.asc());
            } else {
                query.orderBy(employee.id.asc());
            }
        }
        return query;
    }
}
