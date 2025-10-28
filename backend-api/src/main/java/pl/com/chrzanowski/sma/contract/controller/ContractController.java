package pl.com.chrzanowski.sma.contract.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.contract.dto.ContractAuditableDTO;
import pl.com.chrzanowski.sma.contract.dto.ContractDTO;
import pl.com.chrzanowski.sma.contract.service.ContractQueryService;
import pl.com.chrzanowski.sma.contract.service.ContractService;
import pl.com.chrzanowski.sma.contract.service.filter.ContractFilter;

@RestController
@RequestMapping(path = ApiPath.CONTRACT)
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
