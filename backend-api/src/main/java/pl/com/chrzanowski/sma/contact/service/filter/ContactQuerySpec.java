package pl.com.chrzanowski.sma.contact.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.model.QContact;

@Component
public class ContactQuerySpec {

    private final EntityManager em;

    public ContactQuerySpec(EntityManager em) {
        this.em = em;
    }

    public static BooleanBuilder buildPredicate(ContactFilter filter) {
        QContact contact = QContact.contact;
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(contact.id.eq(filter.getId()));
            }
            if (filter.getFirstNameStartsWith() != null && !filter.getFirstNameStartsWith().isEmpty()) {
                predicate.and(contact.firstName.startsWith(filter.getFirstNameStartsWith()));
            }
            if (filter.getLastNameStartsWith() != null && !filter.getLastNameStartsWith().isEmpty()) {
                predicate.and(contact.lastName.startsWith(filter.getLastNameStartsWith()));
            }
            if (filter.getEmailStartsWith() != null && !filter.getEmailStartsWith().isEmpty()) {
                predicate.and(contact.email.startsWith(filter.getEmailStartsWith()));
            }
            if (filter.getPhoneStartsWith() != null && !filter.getPhoneStartsWith().isEmpty()) {
                predicate.and(contact.phoneNumber.startsWith(filter.getPhoneStartsWith()));
            }
        }
        return predicate;
    }

    public JPQLQuery<Contact> buildQuery(BooleanBuilder builder) {
        QContact contact = QContact.contact;

        JPQLQuery<Contact> query = new JPAQuery<>(em).select(contact).from(contact);
        if (builder != null) {
            query.where(builder);
        }
        query.leftJoin(contact.contractors);
        return query;
    }
}
