package pl.com.chrzanowski.sma.contact.service;

import pl.com.chrzanowski.sma.common.service.QueryService;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;

public interface ContactQueryService extends QueryService<ContactDTO, ContactFilter> {
}
