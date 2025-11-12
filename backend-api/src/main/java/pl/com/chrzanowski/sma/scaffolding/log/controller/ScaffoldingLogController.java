package pl.com.chrzanowski.sma.scaffolding.log.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogDTO;
import pl.com.chrzanowski.sma.scaffolding.log.service.ScaffoldingLogQueryService;
import pl.com.chrzanowski.sma.scaffolding.log.service.ScaffoldingLogService;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogFilter;

@RestController
@RequestMapping(path = ApiPath.SCAFFOLDING_LOG)
public class ScaffoldingLogController extends BaseCrudController<
        ScaffoldingLogAuditableDTO,
        ScaffoldingLogDTO,
        ScaffoldingLogDTO,
        ScaffoldingLogDTO,
        Long,
        ScaffoldingLogFilter
        > {

    public ScaffoldingLogController(ScaffoldingLogService service, ScaffoldingLogQueryService queryService) {
        super(service, queryService);
    }

    @Override
    protected Long extractId(ScaffoldingLogDTO scaffoldingLogDTO) {
        return scaffoldingLogDTO.getId();
    }
}
