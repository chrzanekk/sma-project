package pl.com.chrzanowski.sma.constructionsite.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteCreateDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteUpdateDTO;
import pl.com.chrzanowski.sma.constructionsite.service.ConstructionSiteQueryService;
import pl.com.chrzanowski.sma.constructionsite.service.ConstructionSiteService;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/construction-sites")
public class ConstructionSiteController {

    private final Logger log = LoggerFactory.getLogger(ConstructionSiteController.class);

    private final ConstructionSiteService constructionSiteService;
    private final ConstructionSiteQueryService constructionSiteQueryService;

    public ConstructionSiteController(ConstructionSiteService constructionSiteService, ConstructionSiteQueryService constructionSiteQueryService) {
        this.constructionSiteService = constructionSiteService;
        this.constructionSiteQueryService = constructionSiteQueryService;
    }

    @GetMapping("/find")
    public ResponseEntity<List<ConstructionSiteAuditableDTO>> getAllConstructionSitesByFilter(ConstructionSiteFilter filter) {
        log.debug("REST request to get all ConstructionSites by filter: {}", filter.toString());
        List<ConstructionSiteAuditableDTO> constructionSiteAuditableDTOS = constructionSiteQueryService.findByFilter(filter);
        return ResponseEntity.ok().body(constructionSiteAuditableDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ConstructionSiteAuditableDTO>> getAllConstructionSitesByFilterAndPage(ConstructionSiteFilter filter, Pageable pageable) {
        log.debug("REST request to get all ConstructionSites by filter and page: {}", filter.toString());
        Page<ConstructionSiteAuditableDTO> page = constructionSiteQueryService.findByFilter(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ConstructionSiteDTO> getConstructionSiteById(@PathVariable Long id) {
        log.debug("REST request to get ConstructionSite : {}", id);
        ConstructionSiteDTO constructionSiteDTO = constructionSiteService.findById(id);
        return ResponseEntity.ok().body(constructionSiteDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ConstructionSiteDTO> addConstructionSite(@RequestBody ConstructionSiteDTO constructionSiteDTO) {
        log.debug("REST request to add ConstructionSite : {}", constructionSiteDTO);
        ConstructionSiteDTO savedConstructionSiteDTO = constructionSiteService.save(constructionSiteDTO);
        return ResponseEntity.ok().body(savedConstructionSiteDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<ConstructionSiteDTO> createConstructionSite(@RequestBody ConstructionSiteCreateDTO constructionSiteCreateDTO) {
        log.debug("REST request to create ConstructionSite: {}", constructionSiteCreateDTO);
        ConstructionSiteDTO createdConstructionSiteDTO = constructionSiteService.create(constructionSiteCreateDTO);
        return ResponseEntity.ok().body(createdConstructionSiteDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ConstructionSiteDTO> updateConstructionSite(@RequestBody ConstructionSiteDTO constructionSiteDTO) {
        log.debug("REST request to update ConstructionSite : {}", constructionSiteDTO.getId());
        ConstructionSiteDTO updatedConstructionSiteDTO = constructionSiteService.update(constructionSiteDTO);
        return ResponseEntity.ok().body(updatedConstructionSiteDTO);
    }

    @PutMapping("/extended-update")
    public ResponseEntity<ConstructionSiteDTO> extendedUpdateConstructionSite(@RequestBody ConstructionSiteUpdateDTO constructionSiteUpdateDTO) {
        log.debug("REST request to extended update ConstructionSite: {}", constructionSiteUpdateDTO.getId());
        ConstructionSiteDTO updateDTO = constructionSiteService.update(constructionSiteUpdateDTO);
        return ResponseEntity.ok().body(updateDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteConstructionSite(@PathVariable Long id) {
        log.debug("REST request to delete ConstructionSite : {}", id);
        constructionSiteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
