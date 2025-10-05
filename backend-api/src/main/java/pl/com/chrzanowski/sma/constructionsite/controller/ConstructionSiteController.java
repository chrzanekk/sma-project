package pl.com.chrzanowski.sma.constructionsite.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteCreateDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteUpdateDTO;
import pl.com.chrzanowski.sma.constructionsite.service.ConstructionSiteQueryService;
import pl.com.chrzanowski.sma.constructionsite.service.ConstructionSiteService;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteFilter;
import pl.com.chrzanowski.sma.constructionsitecontractor.service.ConstructionSiteContractorService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/api/construction-sites")
public class ConstructionSiteController extends BaseCrudController<
        ConstructionSiteAuditableDTO,
        ConstructionSiteDTO,
        ConstructionSiteDTO,
        ConstructionSiteDTO,
        Long,
        ConstructionSiteFilter
        > {

    private final ConstructionSiteService constructionSiteService;
    private final ConstructionSiteContractorService constructionSiteContractorService;

    public ConstructionSiteController(ConstructionSiteService constructionSiteService,
                                      ConstructionSiteQueryService constructionSiteQueryService, ConstructionSiteService constructionSiteService1, ConstructionSiteContractorService constructionSiteContractorService) {
        super(constructionSiteService, constructionSiteQueryService);
        this.constructionSiteService = constructionSiteService1;
        this.constructionSiteContractorService = constructionSiteContractorService;
    }

    @Override
    protected Long extractId(ConstructionSiteDTO constructionSiteDTO) {
        return constructionSiteDTO.getId();
    }

    @GetMapping("/constructionSite/{constructionSiteId}/contractors")
    public ResponseEntity<List<ContractorDTO>> getPagedContractorsForConstructionSite(
            @PathVariable Long constructionSiteId, Pageable pageable) {
        Page<ContractorDTO> page = constructionSiteContractorService.findAllContractorsByConstructionSiteIdPaged(constructionSiteId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/create")
    public ResponseEntity<ConstructionSiteDTO> createConstructionSite(@RequestBody ConstructionSiteCreateDTO constructionSiteCreateDTO) {
        ConstructionSiteDTO createdConstructionSiteDTO = constructionSiteService.create(constructionSiteCreateDTO);
        return ResponseEntity.ok().body(createdConstructionSiteDTO);
    }

    @PutMapping("/extended-update")
    public ResponseEntity<ConstructionSiteDTO> extendedUpdateConstructionSite(@RequestBody ConstructionSiteUpdateDTO constructionSiteUpdateDTO) {
        ConstructionSiteDTO updateDTO = constructionSiteService.update(constructionSiteUpdateDTO);
        return ResponseEntity.ok().body(updateDTO);
    }
}
