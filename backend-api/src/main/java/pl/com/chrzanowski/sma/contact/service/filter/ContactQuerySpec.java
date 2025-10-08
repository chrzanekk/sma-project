package pl.com.chrzanowski.sma.contact.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.contact.model.Contact;

import static pl.com.chrzanowski.sma.contact.model.QContact.contact;

@Component
public class ContactQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ContactQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    public static BooleanBuilder buildPredicate(ContactFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(contact.id.eq(filter.getId()));
            }
            if (filter.getFirstNameStartsWith() != null && !filter.getFirstNameStartsWith().isEmpty()) {
                predicate.and(contact.firstName.containsIgnoreCase(filter.getFirstNameStartsWith()));
            }
            if (filter.getLastNameStartsWith() != null && !filter.getLastNameStartsWith().isEmpty()) {
                predicate.and(contact.lastName.containsIgnoreCase(filter.getLastNameStartsWith()));
            }
            if (filter.getEmailStartsWith() != null && !filter.getEmailStartsWith().isEmpty()) {
                predicate.and(contact.email.containsIgnoreCase(filter.getEmailStartsWith()));
            }
            if (filter.getPhoneStartsWith() != null && !filter.getPhoneStartsWith().isEmpty()) {
                predicate.and(contact.phoneNumber.containsIgnoreCase(filter.getPhoneStartsWith()));
            }
            if (filter.getCompanyId() != null && filter.getCompanyId() > 0) {
                predicate.and(contact.company.id.eq(filter.getCompanyId()));
            }
            if (filter.getContractorId() != null && filter.getContractorId() > 0) {
                predicate.and(contact.contractor.id.eq(filter.getContractorId()));
            }
            if (filter.getContractorNameStartsWith() != null && !filter.getContractorNameStartsWith().isEmpty()) {
                predicate.and(contact.contractor.name.containsIgnoreCase(filter.getContractorNameStartsWith()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<Contact> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<Contact> query = queryFactory
                .selectFrom(contact)
                .where(builder);

        // Aplikuj sortowanie jeśli jest dostępne
        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "firstName":
                        query.orderBy(order.isAscending() ? contact.firstName.asc() : contact.firstName.desc());
                        break;
                    case "lastName":
                        query.orderBy(order.isAscending() ? contact.lastName.asc() : contact.lastName.desc());
                        break;
                    case "email":
                        query.orderBy(order.isAscending() ? contact.email.asc() : contact.email.desc());
                        break;
                    case "phoneNumber":
                        query.orderBy(order.isAscending() ? contact.phoneNumber.asc() : contact.phoneNumber.desc());
                        break;
                    case "contractor":
                        query.orderBy(order.isAscending() ? contact.contractor.name.asc() : contact.contractor.name.desc());
                        break;
                    case "id":
                        query.orderBy(order.isAscending() ? contact.id.asc() : contact.id.desc());
                        break;
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(contact.id.asc());
            }
        } else {
            // Domyślne sortowanie
            query.orderBy(contact.id.asc());
        }

        return query;
    }
}