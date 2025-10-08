package pl.com.chrzanowski.sma.contact.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
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
import static pl.com.chrzanowski.sma.contract.model.QContract.contract;
import static pl.com.chrzanowski.sma.contractor.model.QContractor.contractor;

@Repository("contactJPA")
public class ContactJPADaoImpl implements ContactDao {

    private final Logger log = LoggerFactory.getLogger(ContactJPADaoImpl.class);

    private final ContactRepository contactRepository;
    private final ContactQuerySpec contactQuerySpec;
    private final BlazeJPAQueryFactory queryFactory;


    public ContactJPADaoImpl(ContactRepository contactRepository, ContactQuerySpec contactQuerySpec, BlazeJPAQueryFactory queryFactory) {
        this.contactRepository = contactRepository;
        this.contactQuerySpec = contactQuerySpec;
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
        BlazeJPAQuery<Contact> baseQuery = contactQuerySpec.buildQuery(specification, pageable);

        baseQuery.leftJoin(contact.contractor, contractor).fetchJoin()
                .leftJoin(contact.company).fetchJoin();

        PagedList<Contact> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public List<Contact> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all contacts by specification: {}", specification);
        return contactQuerySpec.buildQuery(specification, null).fetch();
    }


    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete contact: {}", id);
        contactRepository.deleteById(id);
    }

    @Override
    public Page<Contact> findByContractorId(Long contractorId, Pageable pageable) {
        QContact contact = QContact.contact;
        PagedList<Contact> contacts = queryFactory
                .selectFrom(contact)
                .where(contact.contractor.id.eq(contractorId))
                .orderBy(contact.id.asc())
                .fetchPage((int) pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(contacts, pageable, contacts.getTotalSize());
    }
}
