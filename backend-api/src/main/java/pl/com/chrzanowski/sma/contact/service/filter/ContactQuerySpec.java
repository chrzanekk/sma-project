package pl.com.chrzanowski.sma.contact.service.filter;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.util.query.QueryBuilderUtil;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.model.QContact;

@Component
public class ContactQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ContactQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    public static BooleanBuilder buildPredicate(ContactFilter filter) {
        QContact contact = QContact.contact;
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
        return QueryBuilderUtil.buildQuery(queryFactory, Contact.class, "contact", builder, pageable, QContact.contact.id.asc());
    }
}