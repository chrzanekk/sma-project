package pl.com.chrzanowski.sma.contracts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.contracts.service.ContractQueryService;
import pl.com.chrzanowski.sma.contracts.service.ContractService;

@RestController
@RequestMapping(path = "/api/contracts")
public class ContractController {

    private final Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final ContractService contractService;
    private final ContractQueryService contractQueryService;


    public ContractController(ContractService contractService, ContractQueryService contractQueryService) {
        this.contractService = contractService;
        this.contractQueryService = contractQueryService;
    }

}
