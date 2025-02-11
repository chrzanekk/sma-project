package pl.com.chrzanowski.sma.contact.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.service.ContactQueryService;
import pl.com.chrzanowski.sma.contact.service.ContactService;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/contacts")
public class ContactController {

    private final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;
    private final ContactQueryService contactQueryService;

    public ContactController(ContactService contactService, ContactQueryService contactQueryService) {
        this.contactService = contactService;
        this.contactQueryService = contactQueryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        log.debug("REST request to get all contacts");
        List<ContactDTO> contactDTOS = contactService.findAll();
        return ResponseEntity.ok().body(contactDTOS);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ContactDTO>> getAllContactsByFilter(ContactFilter filter) {
        log.debug("REST request to get all contacts by filter: {}", filter);
        List<ContactDTO> contactBaseDTOS = contactQueryService.findByFilter(filter);
        return ResponseEntity.ok().body(contactBaseDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ContactDTO>> getAllContactsByFilterAndPage(ContactFilter filter, Pageable pageable) {
        log.debug("REST request to get all contacts by filter and page: {}", filter);
        Page<ContactDTO> page = contactQueryService.findByFilter(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        ContactDTO contactDTO = contactService.findById(id);
        return ResponseEntity.ok().body(contactDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ContactDTO> addContact(@RequestBody ContactDTO contactDTO) {
        log.debug("REST request to add Contact : {}", contactDTO);
        ContactDTO savedContactBaseDTO = contactService.save(contactDTO);
        return ResponseEntity.ok().body(savedContactBaseDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contactDTO) {
        log.debug("REST request to update Contact : {}", contactDTO);
        ContactDTO updatedContactDTO = contactService.update(contactDTO);
        return ResponseEntity.ok().body(updatedContactDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        contactService.delete(id);
        return ResponseEntity.ok().build();
    }
}
