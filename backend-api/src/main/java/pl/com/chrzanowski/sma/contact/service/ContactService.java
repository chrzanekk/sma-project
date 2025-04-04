package pl.com.chrzanowski.sma.contact.service;

import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.Collection;
import java.util.List;

public interface ContactService extends BaseCrudService<ContactDTO, Long> {

    List<ContactBaseDTO> saveAllBaseContacts(Collection<ContactBaseDTO> contactDTOs);
}
