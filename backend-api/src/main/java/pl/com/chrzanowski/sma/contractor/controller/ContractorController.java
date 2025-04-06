package pl.com.chrzanowski.sma.contractor.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.service.ContactQueryService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorUpdateDTO;
import pl.com.chrzanowski.sma.contractor.service.ContractorQueryService;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/contractors")
public class ContractorController {

    private final Logger log = LoggerFactory.getLogger(ContractorController.class);

    private final ContractorService contractorService;
    private final ContactQueryService contactQueryService;
    private final ContractorQueryService contractorQueryService;

    public ContractorController(ContractorService contractorService, ContactQueryService contactQueryService, ContractorQueryService contractorQueryService) {
        this.contractorService = contractorService;
        this.contactQueryService = contactQueryService;
        this.contractorQueryService = contractorQueryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContractorDTO>> getAllContractors() {
        log.debug("REST request to get all contractors");
        List<ContractorDTO> contractorDTOS = contractorService.findAll();
        return ResponseEntity.ok().body(contractorDTOS);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ContractorDTO>> getAllContractorsByFilter(ContractorFilter contractorFilter) {
        log.debug("REST request to get all contractors by filter: {}", contractorFilter);
        List<ContractorDTO> contractorDTOS = contractorQueryService.findByFilter(contractorFilter);
        return ResponseEntity.ok().body(contractorDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ContractorDTO>> getAllContractorsByFilterAndPage(ContractorFilter contractorFilter,
                                                                                Pageable pageable) {
        log.debug("REST request to get all contractors by filter with page: {}", contractorFilter);
        Page<ContractorDTO> page = contractorQueryService.findByFilter(contractorFilter, pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ContractorDTO> getContractorById(@Valid @PathVariable Long id) {
        log.debug("REST request to get contractor by id: {}", id);
        ContractorDTO contractorBaseDTO = contractorService.findById(id);
        return ResponseEntity.ok().body(contractorBaseDTO);
    }

    @GetMapping("/contractor/{contractorId}/contacts")
    public ResponseEntity<List<ContactBaseDTO>> getPagedContactsForContractor(
            @PathVariable Long contractorId,
            Pageable pageable) {

        Page<ContactBaseDTO> page = contactQueryService.findByContractorId(contractorId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/add")
    public ResponseEntity<ContractorDTO> addContractor(@RequestBody ContractorDTO contractorDTO) {
        log.debug("REST request to add new contractor: {}", contractorDTO);
        ContractorDTO newContractorBaseDTO = contractorService.save(contractorDTO);
        return ResponseEntity.ok().body(newContractorBaseDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ContractorDTO> updateContractor(@RequestBody ContractorUpdateDTO contractorDTO) {
        log.debug("RST request to update contractor: {}", contractorDTO);
        ContractorDTO updatedContractorDTO = contractorService.updateWithChangedContacts(contractorDTO);
        return ResponseEntity.ok().body(updatedContractorDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContractor(@PathVariable Long id) {
        log.debug("REST request to delete contractor of id: {}", id);
        contractorService.delete(id);
        return ResponseEntity.ok().build();
    }
}
