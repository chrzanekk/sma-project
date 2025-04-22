package pl.com.chrzanowski.sma.contact.service;

import com.querydsl.core.BooleanBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactAuditableDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactAuditMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactDTOMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.model.QContact;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;
import pl.com.chrzanowski.sma.contact.service.filter.ContactQuerySpec;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContactQueryServiceImpl implements ContactQueryService {

    private final Logger log = LoggerFactory.getLogger(ContactQueryServiceImpl.class);

    private final ContactDao contactDao;
    private final ContactAuditMapper contactAuditMapper;
    private final ContactDTOMapper contactDTOMapper;


    public ContactQueryServiceImpl(ContactDao contactDao, ContactAuditMapper contactAuditMapper, ContactDTOMapper contactDTOMapper) {
        this.contactDao = contactDao;
        this.contactAuditMapper = contactAuditMapper;
        this.contactDTOMapper = contactDTOMapper;
    }

    @Override
    @Transactional
    public Page<ContactAuditableDTO> findByFilter(ContactFilter filter, Pageable pageable) {
        log.debug("Query: Find all contacts by filter: {}", filter.toString());
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        return contactDao.findAll(specification, pageable).map(contactAuditMapper::toDto);
    }

    @Override
    @Transactional
    public List<ContactAuditableDTO> findByFilter(ContactFilter filter) {
        log.debug("Query: Find all contacts by filter and page: {}", filter.toString());
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        return contactAuditMapper.toDtoList(contactDao.findAll(specification));
    }

    @Override
    @Transactional
    public Page<ContactDTO> findUnassignedContacts(ContactFilter filter, Pageable pageable) {
        log.debug("Query: Find all free contacts by filter and page: {}", filter.toString());
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        specification.and(QContact.contact.contractor.isNull());
        return contactDao.findAll(specification, pageable).map(contactDTOMapper::toDto);
    }

    @Override
    @Transactional
    public Page<ContactDTO> findByContractorId(Long contractorId, Pageable pageable) {
        Page<Contact> page = contactDao.findByContractorId(contractorId, pageable);
        List<ContactDTO> dtos = contactDTOMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }
}
