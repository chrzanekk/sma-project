package pl.com.chrzanowski.sma.contact.service;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.ContactException;
import pl.com.chrzanowski.sma.common.exception.PropertyMissingException;
import pl.com.chrzanowski.sma.common.exception.error.ContactErrorCode;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactDTOMapper;
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
    private final ContactDTOMapper contactDTOMapper;

    public ContactServiceImpl(ContactDao contactDao, ContactDTOMapper contactDTOMapper) {
        this.contactDao = contactDao;
        this.contactDTOMapper = contactDTOMapper;
    }

    @Override
    @Transactional
    public ContactDTO save(ContactDTO contactBaseDTO) {
        log.debug("Request to save Contact : {}", contactBaseDTO.getEmail());
        validateRequiredFields(contactBaseDTO);
        Contact contact = contactDTOMapper.toEntity(contactBaseDTO);
        Contact savedContact = contactDao.save(contact);
        return contactDTOMapper.toDto(savedContact);
    }

    @Transactional
    @Override
    public List<ContactDTO> saveAllBaseContacts(Collection<ContactDTO> contactDTOS) {
        log.debug("Request to save Contacts.");
        List<Contact> contacts = contactDTOS.stream()
                .peek(this::validateRequiredFields)
                .map(contactDTOMapper::toEntity)
                .collect(Collectors.toList());

        List<Contact> savedContacts = contactDao.saveAll(contacts);

        return savedContacts.stream()
                .map(contactDTOMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactDTO update(ContactDTO contactDTO) {
        log.debug("Update contact: {}", contactDTO.getId());
        validateRequiredFields(contactDTO);
        Contact existingContact = contactDao.findById(contactDTO.getId()).orElseThrow(() -> new ContactException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact with id " + contactDTO.getId() + " not found"));

        contactDTOMapper.updateFromDto(contactDTO, existingContact);
        Contact updatedContact = contactDao.save(existingContact);
        return contactDTOMapper.toDto(updatedContact);
    }

    @Override
    @Transactional
    public ContactDTO findById(Long id) {
        log.debug("Find contact by id: {}", id);
        Optional<Contact> optionalContact = contactDao.findById(id);
        return contactDTOMapper.toDto(optionalContact.orElseThrow(() -> new ContactException(ContactErrorCode.CONTACT_NOT_FOUND, "Contact with id " + id + " not found")));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Delete contact: {}", id);
        contactDao.deleteById(id);
    }

    private void validateRequiredFields(ContactBaseDTO contactBaseDTO) {
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
