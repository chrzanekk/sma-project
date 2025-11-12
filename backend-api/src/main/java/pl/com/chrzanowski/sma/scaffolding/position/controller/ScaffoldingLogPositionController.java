package pl.com.chrzanowski.sma.scaffolding.position.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.service.ScaffoldingLogPositionQueryService;
import pl.com.chrzanowski.sma.scaffolding.position.service.ScaffoldingLogPositionService;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionFilter;

@RestController
@RequestMapping(path = ApiPath.SCAFFOLDING_LOG_POSITION)
public class ScaffoldingLogPositionController extends BaseCrudController<
        ScaffoldingLogPositionAuditableDTO,
        ScaffoldingLogPositionDTO,
        ScaffoldingLogPositionDTO,
        ScaffoldingLogPositionDTO,
        Long,
        ScaffoldingLogPositionFilter> {

    public ScaffoldingLogPositionController(ScaffoldingLogPositionService service, ScaffoldingLogPositionQueryService queryService) {
        super(service, queryService);
    }

    @Override
    protected Long extractId(ScaffoldingLogPositionDTO scaffoldingLogPositionDTO) {
        return scaffoldingLogPositionDTO.getId();
    }
}
