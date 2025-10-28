package pl.com.chrzanowski.sma.position.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.position.dto.PositionAuditableDTO;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.service.PositionQueryService;
import pl.com.chrzanowski.sma.position.service.PositionService;
import pl.com.chrzanowski.sma.position.service.filter.PositionFilter;

@RestController
@RequestMapping(path = ApiPath.POSITION)
public class PositionController extends BaseCrudController<
        PositionAuditableDTO,
        PositionDTO,
        PositionDTO,
        PositionDTO,
        Long,
        PositionFilter
        > {
    public PositionController(PositionService positionService,
                              PositionQueryService queryService) {
        super(positionService, queryService);
    }

    @Override
    protected Long extractId(PositionDTO positionBaseDTO) {
        return positionBaseDTO.getId();
    }
}
