package pl.com.chrzanowski.sma.contact.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactDao contactDao;
    private final ContactMapper contactMapper;
    private final UserService userService;
    private final EntityManager entityManager;

    public ContactServiceImpl(ContactDao contactDao, ContactMapper contactMapper, UserService userService, EntityManager entityManager) {
        this.contactDao = contactDao;
        this.contactMapper = contactMapper;
        this.userService = userService;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ContactDTO save(ContactDTO contactDTO) {
        log.debug("Request to save Contact : {}", contactDTO);

        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();

        Contact contact = contactMapper.toEntity(contactDTO);
        contact.setCreatedBy(entityManager.getReference(User.class, userInfoResponse.id()));
        contact.setModifiedBy(entityManager.getReference(User.class, userInfoResponse.id()));
        contact.setCreatedDatetime(Instant.now());
        contact.setLastModifiedDatetime(Instant.now());
        Contact savedContact = contactDao.save(contact);
        return contactMapper.toDto(savedContact);
    }

    @Override
    @Transactional
    public ContactDTO update(ContactDTO contactDTO) {
        log.debug("Update contact: {}", contactDTO);

        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();

        Contact existingContact = contactDao.findById(contactDTO.getId()).orElseThrow(() -> new ObjectNotFoundException("Contact with id " + contactDTO.getId() + " not found"));

        Contact contact = contactMapper.toEntity(contactDTO);
        contact.setId(existingContact.getId());
        contact.setCreatedBy(existingContact.getCreatedBy());
        contact.setCreatedDatetime(existingContact.getCreatedDatetime());
        contact.setModifiedBy(entityManager.getReference(User.class, userInfoResponse.id()));
        contact.setLastModifiedDatetime(Instant.now());
        Contact updatedContact = contactDao.save(contact);
        return contactMapper.toDto(updatedContact);
    }

    @Override
    public ContactDTO findById(Long id) {
        log.debug("Find contact by id: {}", id);
        Optional<Contact> optionalContact = contactDao.findById(id);
        return contactMapper.toDto(optionalContact.orElseThrow(() -> new ObjectNotFoundException("Contact with id " + id + " not found")));
    }

    @Override
    public List<ContactDTO> findAll() {
        log.debug("Find all contacts");
        List<Contact> contacts = contactDao.findAll();
        return contactMapper.toDtoList(contacts);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete contact: {}", id);
        contactDao.deleteById(id);
    }
}
