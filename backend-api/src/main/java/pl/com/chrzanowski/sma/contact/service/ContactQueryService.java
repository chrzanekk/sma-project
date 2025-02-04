package pl.com.chrzanowski.sma.contact.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;

import java.util.List;

public interface ContactQueryService {

    Page<ContactDTO> findByFilter(ContactFilter filter, Pageable pageable);

    List<ContactDTO> findByFilter(ContactFilter filter);
}
