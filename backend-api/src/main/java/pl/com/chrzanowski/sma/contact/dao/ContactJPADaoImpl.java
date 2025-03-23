package pl.com.chrzanowski.sma.contact.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.repository.ContactRepository;
import pl.com.chrzanowski.sma.contact.service.filter.ContactQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("contactJPA")
public class ContactJPADaoImpl implements ContactDao {

    private final Logger log = LoggerFactory.getLogger(ContactJPADaoImpl.class);

    private final ContactRepository contactRepository;
    private final ContactQuerySpec contactQuerySpec;
    private final EntityManager em;

    public ContactJPADaoImpl(ContactRepository contactRepository, ContactQuerySpec contactQuerySpec, EntityManager em) {
        this.contactRepository = contactRepository;
        this.contactQuerySpec = contactQuerySpec;
        this.em = em;
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
        log.debug("DAO: Find all contacts by specificaiton with page: {}", specification);
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

        EntityGraph<Contact> entityGraph = em.createEntityGraph(Contact.class);
        entityGraph.addSubgraph("contractors");

        jpaQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);
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
}
