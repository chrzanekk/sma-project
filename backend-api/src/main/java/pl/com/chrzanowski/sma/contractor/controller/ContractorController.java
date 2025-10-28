package pl.com.chrzanowski.sma.contractor.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.service.ContactQueryService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorAuditableDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorUpdateDTO;
import pl.com.chrzanowski.sma.contractor.service.ContractorQueryService;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.CONTRACTOR)
public class ContractorController extends BaseCrudController<
        ContractorAuditableDTO,
        ContractorDTO,
        ContractorDTO,
        ContractorUpdateDTO,
        Long,
        ContractorFilter
        > {

    private final ContactQueryService contactQueryService;

    public ContractorController(ContractorService contractorService,
                                ContractorQueryService contractorQueryService,
                                ContactQueryService contactQueryService) {
        super(contractorService, contractorQueryService);
        this.contactQueryService = contactQueryService;
    }

    @Override
    protected Long extractId(ContractorDTO contractorDTO) {
        return contractorDTO.getId();
    }

    @GetMapping("/{contractorId}/contacts")
    public ResponseEntity<List<ContactDTO>> getPagedContactsForContractor(
            @PathVariable Long contractorId,
            Pageable pageable) {

        Page<ContactDTO> page = contactQueryService.findByContractorId(contractorId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
