package pl.com.chrzanowski.sma.contact.service;


import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.exception.ContactException;
import pl.com.chrzanowski.sma.common.exception.PropertyMissingException;
import pl.com.chrzanowski.sma.common.exception.error.ContactErrorCode;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.AbstractContactDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactDao contactDao;
    private final ContactMapper contactMapper;
    private final ContactBaseMapper contactBaseMapper;

    public ContactServiceImpl(ContactDao contactDao, ContactMapper contactMapper, ContactBaseMapper contactBaseMapper) {
        this.contactDao = contactDao;
        this.contactMapper = contactMapper;
        this.contactBaseMapper = contactBaseMapper;
    }

    @Override
    @Transactional
    public ContactBaseDTO save(ContactBaseDTO contactBaseDTO) {
        log.debug("Request to save Contact : {}", contactBaseDTO.getId());
        validateRequiredFields(contactBaseDTO);
        Contact contact = contactBaseMapper.toEntity(contactBaseDTO);
        Contact savedContact = contactDao.save(contact);
        return contactBaseMapper.toDto(savedContact);
    }

    //todo test this method in service and dao
    @Transactional
    @Override
    public List<ContactBaseDTO> saveAllBaseContacts(Collection<ContactBaseDTO> ContactBaseDTOs) {
        log.debug("Request to save Contacts.");
        List<Contact> contacts = ContactBaseDTOs.stream()
                .peek(this::validateRequiredFields)
                .map(contactBaseMapper::toEntity)
                .collect(Collectors.toList());

        List<Contact> savedContacts = contactDao.saveAll(contacts);

        return savedContacts.stream()
                .map(contactBaseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactBaseDTO update(ContactBaseDTO ContactBaseDTO) {
        log.debug("Update contact: {}", ContactBaseDTO.getId());
        validateRequiredFields(ContactBaseDTO);
        Contact existingContact = contactDao.findById(ContactBaseDTO.getId()).orElseThrow(() -> new ContactException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact with id " + ContactBaseDTO.getId() + " not found"));

        contactBaseMapper.updateContactFromDto(ContactBaseDTO, existingContact);
        Contact updatedContact = contactDao.save(existingContact);
        return contactBaseMapper.toDto(updatedContact);
    }

    @Override
    public ContactBaseDTO findById(Long id) {
        log.debug("Find contact by id: {}", id);
        Optional<Contact> optionalContact = contactDao.findById(id);
        return contactBaseMapper.toDto(optionalContact.orElseThrow(() -> new ContactException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact with id " + id + " not found")));
    }

    @Override
    public List<ContactBaseDTO> findAll() {
        log.debug("Find all contacts");
        List<Contact> contacts = contactDao.findAll();
        return contactBaseMapper.toDtoList(contacts);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete contact: {}", id);
        contactDao.deleteById(id);
    }

    private void validateRequiredFields(AbstractContactDTO contactBaseDTO) {
        if (StringUtils.isBlank(contactBaseDTO.getFirstName())) {
            throw new PropertyMissingException(ContactErrorCode.FIRST_NAME_MISSING, "First name must not be empty", Map.of("firstName", contactBaseDTO.getFirstName()));
        }
        if (StringUtils.isBlank(contactBaseDTO.getLastName())) {
            throw new PropertyMissingException(ContactErrorCode.LAST_NAME_MISSING, "Last name must not be empty", Map.of("lastName", contactBaseDTO.getLastName()));
        }
        if (StringUtils.isBlank(contactBaseDTO.getEmail())) {
            throw new PropertyMissingException(ContactErrorCode.EMAIL_MISSING, "Email must not be empty", Map.of("email", contactBaseDTO.getEmail()));
        }
        if (StringUtils.isBlank(contactBaseDTO.getPhoneNumber())) {
            throw new PropertyMissingException(ContactErrorCode.PHONE_NUMBER_MISSING, "PhoneNumber must not be empty", Map.of("phoneNumber", contactBaseDTO.getPhoneNumber()));
        }
    }
}
