package pl.com.chrzanowski.sma.company.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.company.dto.CompanyAuditableDTO;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.service.CompanyQueryService;
import pl.com.chrzanowski.sma.company.service.CompanyService;
import pl.com.chrzanowski.sma.company.service.filter.CompanyFilter;

@RestController
@RequestMapping(ApiPath.COMPANY)
public class CompanyController extends BaseCrudController<CompanyAuditableDTO, CompanyDTO, CompanyDTO, CompanyDTO, Long, CompanyFilter> {


    public CompanyController(CompanyService companyService, CompanyQueryService companyQueryService) {
        super(companyService, companyQueryService);

    }

    @Override
    protected Long extractId(CompanyDTO companyDTO) {
        return companyDTO.getId();
    }
}

