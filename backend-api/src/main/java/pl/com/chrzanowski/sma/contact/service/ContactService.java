package pl.com.chrzanowski.sma.contact.service;

import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.Collection;
import java.util.List;

public interface ContactService {

    ContactDTO save(ContactDTO contactBaseDTO);

    List<ContactBaseDTO> saveAllBaseContacts(Collection<ContactBaseDTO> contactDTOs);

    ContactDTO update(ContactDTO contactBaseDTO);

    ContactDTO findById(Long id);

    List<ContactDTO> findAll();

    void delete(Long id);
}
