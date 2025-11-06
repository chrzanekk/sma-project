package pl.com.chrzanowski.sma.scaffolding.worktype.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.chrzanowski.sma.common.controller.BaseCrudController;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.WorkTypeQueryService;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.WorkTypeService;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeFilter;
@RestController
@RequestMapping(path = ApiPath.WORK_TYPE)
public class WorkTypeController extends BaseCrudController<WorkTypeAuditableDTO, WorkTypeDTO, WorkTypeDTO, WorkTypeDTO, Long, WorkTypeFilter> {
    public WorkTypeController(WorkTypeService workTypeService, WorkTypeQueryService workTypeQueryService) {
        super(workTypeService, workTypeQueryService);
    }

    @Override
    protected Long extractId(WorkTypeDTO workTypeDTO) {
        return workTypeDTO.getId();
    }
}
