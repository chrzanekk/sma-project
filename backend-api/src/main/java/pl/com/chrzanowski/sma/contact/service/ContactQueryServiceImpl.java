package pl.com.chrzanowski.sma.contact.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.model.QContact;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;
import pl.com.chrzanowski.sma.contact.service.filter.ContactQuerySpec;

import java.util.List;

@Service
@Transactional
public class ContactQueryServiceImpl implements ContactQueryService {

    private final Logger log = LoggerFactory.getLogger(ContactQueryServiceImpl.class);

    private final ContactDao contactDao;
    private final ContactMapper contactMapper;
    private final ContactBaseMapper contactBaseMapper;
    private final ContactQuerySpec contactQuerySpec;

    public ContactQueryServiceImpl(ContactDao contactDao, ContactMapper contactMapper, ContactBaseMapper contactBaseMapper, ContactQuerySpec contactQuerySpec) {
        this.contactDao = contactDao;
        this.contactMapper = contactMapper;
        this.contactBaseMapper = contactBaseMapper;
        this.contactQuerySpec = contactQuerySpec;
    }

    @Override
    @Transactional
    public Page<ContactDTO> findByFilter(ContactFilter filter, Pageable pageable) {
        log.debug("Query: Find all contacts by filter: {}", filter.toString());
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        return contactDao.findAll(specification, pageable).map(contactMapper::toDto);
    }

    @Override
    @Transactional
    public List<ContactDTO> findByFilter(ContactFilter filter) {
        log.debug("Query: Find all contacts by filter and page: {}", filter.toString());
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        return contactMapper.toDtoList(contactDao.findAll(specification));
    }

    @Override
    @Transactional
    public Page<ContactBaseDTO> findUnassignedContacts(ContactFilter filter, Pageable pageable) {
        log.debug("Query: Find all free contacts by filter and page: {}", filter.toString());
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        specification.and(QContact.contact.contractor.isNull());
        return contactDao.findAll(specification, pageable).map(contactBaseMapper::toDto);
    }

    @Override
    public Page<ContactBaseDTO> findByContractorId(Long contractorId, Pageable pageable) {
        Page<Contact> page = contactDao.findByContractorId(contractorId, pageable);
        List<ContactBaseDTO> dtos = contactBaseMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }
}
