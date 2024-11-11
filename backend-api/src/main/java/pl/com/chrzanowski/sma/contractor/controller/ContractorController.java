package pl.com.chrzanowski.sma.contractor.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.service.ContractorQueryService;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/contractors")
public class ContractorController {

    private final Logger log = LoggerFactory.getLogger(ContractorController.class);

    private final ContractorService contractorService;
    private final ContractorQueryService contractorQueryService;

    public ContractorController(ContractorService contractorService, ContractorQueryService contractorQueryService) {
        this.contractorService = contractorService;
        this.contractorQueryService = contractorQueryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContractorDTO>> getAllContractors() {
        log.debug("REST request to get all workshops");
        List<ContractorDTO> contractorDTOS = contractorService.findAll();
        return ResponseEntity.ok().body(contractorDTOS);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ContractorDTO>> getAllContractorsByFilter(ContractorFilter contractorFilter) {
        log.debug("REST request to get all workshops by filter: {}", contractorFilter);
        List<ContractorDTO> contractorDTOS = contractorQueryService.findByFilter(contractorFilter);
        return ResponseEntity.ok().body(contractorDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ContractorDTO>> getAllContractorsByFilterAndPage(ContractorFilter contractorFilter,
                                                                                Pageable pageable) {
        log.debug("REST request to get all workshops by filter: {}", contractorFilter);
        Page<ContractorDTO> page = contractorQueryService.findByFilter(contractorFilter, pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ContractorDTO> getContractorById(@Valid @PathVariable Long id) {
        log.debug("REST request to get workshop by id: {}", id);
        ContractorDTO contractorDTO = contractorService.findById(id);
        return ResponseEntity.ok().body(contractorDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ContractorDTO> addContractor(@RequestBody ContractorDTO contractorDTO) {
        log.debug("REST request to add new workshop: {}", contractorDTO);
        ContractorDTO newContractorDTO = contractorService.save(contractorDTO);
        return ResponseEntity.ok().body(newContractorDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ContractorDTO> updateContractor(@RequestBody ContractorDTO contractorDTO) {
        log.debug("RST request to update workshop: {}", contractorDTO);
        ContractorDTO updatedContractorDTO = contractorService.update(contractorDTO);
        return ResponseEntity.ok().body(updatedContractorDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContractor(@PathVariable Long id) {
        log.debug("REST request to delete workshop of id: {}", id);
        contractorService.delete(id);
        return ResponseEntity.ok().build();
    }
}
