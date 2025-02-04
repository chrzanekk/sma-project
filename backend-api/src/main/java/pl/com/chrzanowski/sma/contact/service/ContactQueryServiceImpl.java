package pl.com.chrzanowski.sma.contact.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactMapper;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;
import pl.com.chrzanowski.sma.contact.service.filter.ContactQuerySpec;

import java.util.List;

@Service
@Transactional
public class ContactQueryServiceImpl implements ContactQueryService {

    private final Logger log = LoggerFactory.getLogger(ContactQueryServiceImpl.class);

    private final ContactDao contactDao;
    private final ContactMapper contactMapper;

    public ContactQueryServiceImpl(ContactDao contactDao, ContactMapper contactMapper) {
        this.contactDao = contactDao;
        this.contactMapper = contactMapper;
    }

    @Override
    @Transactional
    public Page<ContactDTO> findByFilter(ContactFilter filter, Pageable pageable) {
        log.debug("Query: Find all contacts by filter: {}", filter);
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        return contactDao.findAll(specification, pageable).map(contactMapper::toDto);
    }

    @Override
    @Transactional
    public List<ContactDTO> findByFilter(ContactFilter filter) {
        log.debug("Query: Find all contacts by filter and page: {}", filter);
        BooleanBuilder specification = ContactQuerySpec.buildPredicate(filter);
        return contactMapper.toDtoList(contactDao.findAll(specification));
    }
}
