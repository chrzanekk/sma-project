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
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
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



    @GetMapping("/find")
    public ResponseEntity<List<ContactBaseDTO>> getAllContactsByFilter(ContactFilter filter) {
        log.debug("REST request to get all contacts by filter: {}", filter);
        List<ContactBaseDTO> contactBaseDTOS = contactQueryService.findByFilter(filter);
        return ResponseEntity.ok().body(contactBaseDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ContactBaseDTO>> getAllContactsByFilterAndPage(ContactFilter filter, Pageable pageable) {
        log.debug("REST request to get all contacts by filter and page: {}", filter);
        Page<ContactBaseDTO> page = contactQueryService.findByFilter(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ContactBaseDTO> getContactById(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        ContactBaseDTO contactDTO = contactService.findById(id);
        return ResponseEntity.ok().body(contactDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ContactBaseDTO> addContact(@RequestBody ContactBaseDTO contactDTO) {
        log.debug("REST request to add Contact : {}", contactDTO);
        ContactBaseDTO savedContactBaseDTO = contactService.save(contactDTO);
        return ResponseEntity.ok().body(savedContactBaseDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ContactBaseDTO> updateContact(@RequestBody ContactBaseDTO contactDTO) {
        log.debug("REST request to update Contact : {}", contactDTO);
        ContactBaseDTO updatedContactDTO = contactService.update(contactDTO);
        return ResponseEntity.ok().body(updatedContactDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        contactService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/free/page")
    public ResponseEntity<Page<ContactBaseDTO>> getFreeContactsPage(ContactFilter filter, Pageable pageable) {
        Page<ContactBaseDTO> page = contactQueryService.findUnassignedContacts(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }
}
