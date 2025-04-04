package pl.com.chrzanowski.sma.contact.service.filter;

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
            if (filter.getCompanyId() != null && filter.getCompanyId() > 0) {
                predicate.and(contact.company.id.eq(filter.getCompanyId()));
            }
        }
        return predicate;
    }

    public JPQLQuery<Contact> buildQuery(BooleanBuilder builder, Pageable pageable) {
        QContact contact = QContact.contact;

        JPQLQuery<Contact> query = new JPAQuery<>(em).select(contact).distinct().from(contact);
        if (builder != null) {
            query.where(builder);
        }

        if (pageable != null && pageable.getSort().isSorted()) {
            PathBuilder<Contact> contactPathBuilder = new PathBuilder<>(Contact.class, "contact");
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        contactPathBuilder.getComparable(order.getProperty(), String.class)
                );
                query.orderBy(orderSpecifier);
            }
        }
        return query;
    }
}
