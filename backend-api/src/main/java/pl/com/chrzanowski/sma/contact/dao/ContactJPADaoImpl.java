package pl.com.chrzanowski.sma.contact.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
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

    public ContactJPADaoImpl(ContactRepository contactRepository, ContactQuerySpec contactQuerySpec) {
        this.contactRepository = contactRepository;
        this.contactQuerySpec = contactQuerySpec;
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("DAO: Save contact: {}", contact);
        return contactRepository.save(contact);
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
        JPQLQuery<Contact> query = contactQuerySpec.buildQuery(specification, pageable);
        long totalElements = query.fetchCount();
        List<Contact> content = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        return new PageImpl<>(content, pageable, totalElements);
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
