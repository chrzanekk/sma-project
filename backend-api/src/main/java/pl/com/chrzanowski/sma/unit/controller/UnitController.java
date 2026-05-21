package pl.com.chrzanowski.sma.unit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.unit.dto.UnitAuditableDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.service.UnitQueryService;
import pl.com.chrzanowski.sma.unit.service.UnitService;
import pl.com.chrzanowski.sma.unit.service.filter.UnitFilter;

@RestController
@RequestMapping(path = ApiPath.UNIT)
public class UnitController extends BaseCrudController<
        UnitAuditableDTO,
        UnitDTO,
        UnitDTO,
        UnitDTO,
        Long,
        UnitFilter
        > {
    public UnitController(UnitService unitService,
                          UnitQueryService queryService) {
        super(unitService, queryService);
    }

    @Override
    protected Long extractId(UnitDTO unitDTO) {
        return unitDTO.getId();
    }
}
