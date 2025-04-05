package pl.com.chrzanowski.sma.contact.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;

public interface ContactQueryService extends QueryService<ContactDTO, ContactFilter> {

    Page<ContactBaseDTO> findUnassignedContacts(ContactFilter contactFilter, Pageable pageable);
}
