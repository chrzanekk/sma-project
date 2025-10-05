package pl.com.chrzanowski.sma.contact.service;

import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.Collection;
import java.util.List;

public interface ContactService extends BaseCrudService<ContactDTO, ContactDTO, ContactDTO, Long> {

    List<ContactDTO> saveAllBaseContacts(Collection<ContactDTO> contactDTOs);
}
