package pl.com.chrzanowski.sma.scaffolding.position.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final ScaffoldingLogPositionService positionService;

    public ScaffoldingLogPositionController(ScaffoldingLogPositionService service, ScaffoldingLogPositionQueryService queryService) {
        super(service, queryService);
        this.positionService = service;
    }

    @Override
    protected Long extractId(ScaffoldingLogPositionDTO scaffoldingLogPositionDTO) {
        return scaffoldingLogPositionDTO.getId();
    }

    @GetMapping("/next-base-number")
    public ResponseEntity<Integer> getNexBaseScaffoldingNumber(@RequestParam Long logId,
                                                               @RequestParam int year) {
        Integer nextBaseScaffoldingNumber = positionService.getNextBaseScaffoldingLogNumber(logId, year);
        return ResponseEntity.ok().body(nextBaseScaffoldingNumber);
    }
}
