package pl.com.chrzanowski.sma.contact.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.model.QContact;
import pl.com.chrzanowski.sma.contact.repository.ContactRepository;
import pl.com.chrzanowski.sma.contact.service.filter.ContactQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.contact.model.QContact.contact;
import static pl.com.chrzanowski.sma.contractor.model.QContractor.contractor;

@Repository("contactJPA")
public class ContactJPADaoImpl implements ContactDao {

    private final Logger log = LoggerFactory.getLogger(ContactJPADaoImpl.class);

    private final ContactRepository contactRepository;
    private final ContactQuerySpec contactQuerySpec;
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;


    public ContactJPADaoImpl(ContactRepository contactRepository, ContactQuerySpec contactQuerySpec, EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.contactRepository = contactRepository;
        this.contactQuerySpec = contactQuerySpec;

        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("DAO: Save contact: {}", contact.getId());
        return contactRepository.save(contact);
    }

    @Override
    public List<Contact> saveAll(List<Contact> contacts) {
        log.debug("DAO: Save all contacts");
        return contactRepository.saveAll(contacts);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        log.debug("DAO: Find contact: {}", id);
        return contactRepository.findById(id);
    }

    @Override
    public List<Contact> findAll() {
        log.debug("DAO: Find all contacts");
        return contactRepository.findAll();
    }

    @Override
    public Page<Contact> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all contacts by specification with page: {}", specification);
        JPQLQuery<Contact> baseQuery = contactQuerySpec.buildQuery(specification, pageable);

        long totalElements = baseQuery.fetchCount();

        JPAQuery<Contact> jpaQuery = getPaginationQuery(baseQuery);

        List<Contact> content = jpaQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(content, pageable, totalElements);
    }

    private JPAQuery<Contact> getPaginationQuery(JPQLQuery<Contact> baseQuery) {
        JPAQuery<Contact> jpaQuery = (JPAQuery<Contact>) baseQuery;

        jpaQuery
                .leftJoin(contact.contractor, contractor).fetchJoin()
                .leftJoin(contractor.company).fetchJoin();
        return jpaQuery;
    }

    @Override
    public List<Contact> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all contacts by specificaiton: {}", specification);
        JPQLQuery<Contact> query = contactQuerySpec.buildQuery(specification, null);
        return query.fetch();
    }


    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete contact: {}", id);
        contactRepository.deleteById(id);
    }

    @Override
    public Page<Contact> findByContractorId(Long contractorId, Pageable pageable) {
        QContact contact = QContact.contact;
        List<Contact> contacts = queryFactory
                .selectFrom(contact)
                .where(contact.contractor.id.eq(contractorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(contact.id.asc()) // lub inna kolumna jeśli trzeba
                .fetch();

        Long total = queryFactory
                .select(contact.count())
                .from(contact)
                .where(contact.contractor.id.eq(contractorId))
                .fetchOne();

        return new PageImpl<>(contacts, pageable, total != null ? total : 0);
    }
}
