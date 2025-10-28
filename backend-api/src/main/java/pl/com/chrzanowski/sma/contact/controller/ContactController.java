package pl.com.chrzanowski.sma.contact.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.contact.dto.ContactAuditableDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.service.ContactQueryService;
import pl.com.chrzanowski.sma.contact.service.ContactService;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;

@RestController
@RequestMapping(path = ApiPath.CONTACT)
public class ContactController extends BaseCrudController<
        ContactAuditableDTO,
        ContactDTO,
        ContactDTO,
        ContactDTO,
        Long,
        ContactFilter
        > {

    private final ContactQueryService contactQueryService;

    public ContactController(ContactService contactService,
                             ContactQueryService contactQueryService) {
        super(contactService, contactQueryService);
        this.contactQueryService = contactQueryService;
    }


    @GetMapping("/free/page")
    public ResponseEntity<Page<ContactDTO>> getFreeContactsPage(ContactFilter filter, Pageable pageable) {
        Page<ContactDTO> page = contactQueryService.findUnassignedContacts(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @Override
    protected Long extractId(ContactDTO contactDTO) {
        return contactDTO.getId();
    }
}
