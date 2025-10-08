package pl.com.chrzanowski.sma.contract.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.contract.model.Contract;

import static pl.com.chrzanowski.sma.contract.model.QContract.contract;

@Component
public class ContractQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ContractQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ContractFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(contract.id.eq(filter.getId()));
            }
            if (filter.getNumberStartsWith() != null && !filter.getNumberStartsWith().isEmpty()) {
                predicate.and(contract.number.containsIgnoreCase(filter.getNumberStartsWith()));
            }
            if (filter.getDescriptionStartsWith() != null && !filter.getDescriptionStartsWith().isEmpty()) {
                predicate.and(contract.description.containsIgnoreCase(filter.getDescriptionStartsWith()));
            }
            if (filter.getValueStartsWith() != null) {
                predicate.and(contract.value.goe(filter.getValueStartsWith()));
            }
            if (filter.getValueEndsWith() != null) {
                predicate.and(contract.value.loe(filter.getValueEndsWith()));
            }
            if (filter.getStartDateStartWith() != null) {
                predicate.and(contract.startDate.goe(filter.getStartDateStartWith()));
            }
            if (filter.getStartDateEndWith() != null) {
                predicate.and(contract.startDate.loe(filter.getStartDateEndWith()));
            }
            if (filter.getEndDateStartWith() != null) {
                predicate.and(contract.endDate.goe(filter.getEndDateStartWith()));
            }
            if (filter.getEndDateEndWith() != null) {
                predicate.and(contract.endDate.loe(filter.getEndDateEndWith()));
            }
            if (filter.getSignUpDateStartWith() != null) {
                predicate.and(contract.signupDate.goe(filter.getSignUpDateStartWith()));
            }
            if (filter.getSignUpDateEndWith() != null) {
                predicate.and(contract.signupDate.loe(filter.getSignUpDateEndWith()));
            }
            if (filter.getContractorId() != null) {
                predicate.and(contract.contractor.id.eq(filter.getContractorId()));
            }
            if (filter.getConstructionSiteId() != null) {
                predicate.and(contract.constructionSite.id.eq(filter.getConstructionSiteId()));
            }
            if (filter.getCompanyId() != null) {
                predicate.and(contract.company.id.eq(filter.getCompanyId()));
            }
            if (filter.getContractorNameStartsWith() != null) {
                predicate.and(contract.contractor.name.containsIgnoreCase(filter.getContractorNameStartsWith()));
            }
            if (filter.getConstructionSiteNameStartsWith() != null) {
                predicate.and(contract.constructionSite.name.containsIgnoreCase(filter.getConstructionSiteNameStartsWith()));
            }
            if (filter.getContactNameStartsWith() != null) {
                predicate.and(contract.contact.lastName.containsIgnoreCase(filter.getContactNameStartsWith()));
            }
        }

        return predicate;
    }

    public BlazeJPAQuery<Contract> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Contract> query = queryFactory
                .selectFrom(contract)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "number":
                        query.orderBy(order.isAscending() ? contract.number.asc() : contract.number.desc());
                        break;
                    case "description":
                        query.orderBy(order.isAscending() ? contract.description.asc() : contract.description.desc());
                        break;
                    case "value":
                        query.orderBy(order.isAscending() ? contract.value.asc() : contract.value.desc());
                        break;
                    case "startDate":
                        query.orderBy(order.isAscending() ? contract.startDate.asc() : contract.startDate.desc());
                        break;
                    case "endDate":
                        query.orderBy(order.isAscending() ? contract.endDate.asc() : contract.endDate.desc());
                        break;
                    case "signupDate":
                        query.orderBy(order.isAscending() ? contract.signupDate.asc() : contract.signupDate.desc());
                        break;
                    case "realEndDate":
                        query.orderBy(order.isAscending() ? contract.realEndDate.asc() : contract.realEndDate.desc());
                        break;
                    case "id":
                        query.orderBy(order.isAscending() ? contract.id.asc() : contract.id.desc());
                        break;
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(contract.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(contract.id.asc());
        }

        return query;
    }
}

