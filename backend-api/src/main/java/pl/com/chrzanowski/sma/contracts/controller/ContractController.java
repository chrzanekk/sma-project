package pl.com.chrzanowski.sma.contracts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.contracts.dto.ContractAuditableDTO;
import pl.com.chrzanowski.sma.contracts.dto.ContractDTO;
import pl.com.chrzanowski.sma.contracts.service.ContractQueryService;
import pl.com.chrzanowski.sma.contracts.service.ContractService;
import pl.com.chrzanowski.sma.contracts.service.filter.ContractFilter;

@RestController
@RequestMapping(path = "/api/contracts")
public class ContractController extends BaseCrudController<
        ContractAuditableDTO,
        ContractDTO,
        ContractDTO,
        ContractDTO,
        Long,
        ContractFilter
        > {

    public ContractController(ContractService contractService,
                              ContractQueryService contractQueryService) {
        super(contractService, contractQueryService);
    }

    @Override
    protected Long extractId(ContractDTO contractDTO) {
        return contractDTO.getId();
    }
}
