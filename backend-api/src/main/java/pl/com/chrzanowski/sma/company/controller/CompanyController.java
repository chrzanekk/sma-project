package pl.com.chrzanowski.sma.company.controller;

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
import pl.com.chrzanowski.sma.company.dto.CompanyAuditableDTO;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.service.CompanyQueryService;
import pl.com.chrzanowski.sma.company.service.CompanyService;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;
    private final CompanyQueryService companyQueryService;

    public CompanyController(CompanyService companyService, CompanyQueryService companyQueryService) {
        this.companyService = companyService;
        this.companyQueryService = companyQueryService;
    }

    @GetMapping("/find")
    public ResponseEntity<List<CompanyAuditableDTO>> getAllByFilter(CompanyFilter filter) {
        log.info("REST: request to get all companies by filter: {}", filter);
        List<CompanyAuditableDTO> companyBaseDTOS = companyQueryService.findByFilter(filter);
        return ResponseEntity.ok(companyBaseDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<CompanyAuditableDTO>> getAllByFilter(CompanyFilter filter, Pageable pageable) {
        log.info("REST: request to get all companies by filter with page: {}", filter);
        Page<CompanyAuditableDTO> page = companyQueryService.findByFilter(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CompanyBaseDTO> getById(@Valid @PathVariable Long id) {
        log.debug("REST: request to get company by id: {}", id);
        CompanyBaseDTO companyBaseDTO = companyService.findById(id);
        return ResponseEntity.ok(companyBaseDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CompanyBaseDTO> add(@RequestBody CompanyBaseDTO companyBaseDTO) {
        log.debug("REST: request to save company: {}", companyBaseDTO.getId());
        CompanyBaseDTO companyBaseDTOSaved = companyService.save(companyBaseDTO);
        return ResponseEntity.ok(companyBaseDTOSaved);
    }

    @PutMapping("/update")
    public ResponseEntity<CompanyBaseDTO> update(@RequestBody CompanyBaseDTO companyBaseDTO) {
        log.debug("REST: request to update company: {}", companyBaseDTO.getId());
        CompanyBaseDTO companyBaseDTOUpdated = companyService.update(companyBaseDTO);
        return ResponseEntity.ok(companyBaseDTOUpdated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST: request to delete company: {}", id);
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
