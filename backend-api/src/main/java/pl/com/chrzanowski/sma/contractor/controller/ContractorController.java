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
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/workshops")
public class ContractorController {

    private final Logger log = LoggerFactory.getLogger(ContractorController.class);
    private static final String ENTITY_NAME = "workshop";
    private final ContractorService contractorService;

    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContractorDTO>> getAllWorkshops() {
        log.debug("REST request to get all workshops");
        List<ContractorDTO> contractorDTOS = contractorService.findAll();
        return ResponseEntity.ok().body(contractorDTOS);
    }

    @GetMapping("/")
    public ResponseEntity<List<ContractorDTO>> getAllWorkshopsByFilter(ContractorFilter contractorFilter) {
        log.debug("REST request to get all workshops by filter: {}", contractorFilter);
        List<ContractorDTO> contractorDTOS = contractorService.findByFilter(contractorFilter);
        return ResponseEntity.ok().body(contractorDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ContractorDTO>> getAllWorkshopsByFilterAndPage(ContractorFilter contractorFilter,
                                                                              Pageable pageable) {
        log.debug("REST request to get all workshops by filter: {}", contractorFilter);
        Page<ContractorDTO> page = contractorService.findByFilterAndPage(contractorFilter,
                pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ContractorDTO> getWorkshopById(@Valid @PathVariable Long id) {
        log.debug("REST request to get workshop by id: {}", id);
        ContractorDTO contractorDTO = contractorService.findById(id);
        return ResponseEntity.ok().body(contractorDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ContractorDTO> addWorkshop(@RequestBody ContractorDTO contractorDTO) {
        log.debug("REST request to add new workshop: {}", contractorDTO);
        ContractorDTO newContractorDTO = contractorService.save(contractorDTO);
        return ResponseEntity.ok().body(newContractorDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ContractorDTO> updateWorkshop(@RequestBody ContractorDTO contractorDTO) {
        log.debug("RST request to update workshop: {}", contractorDTO);
        ContractorDTO updatedContractorDTO = contractorService.update(contractorDTO);
        return ResponseEntity.ok().body(updatedContractorDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkshop(@PathVariable Long id) {
        log.debug("REST request to delete workshop of id: {}", id);
        contractorService.delete(id);
        return ResponseEntity.ok().build();
    }
}
