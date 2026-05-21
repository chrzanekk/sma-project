package pl.com.chrzanowski.sma.scaffolding.dimension.service;

import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;

import java.util.List;

public interface ScaffoldingLogPositionDimensionService extends BaseCrudService<
        ScaffoldingLogPositionDimensionDTO,
        ScaffoldingLogPositionDimensionDTO,
        ScaffoldingLogPositionDimensionDTO,
        Long> {

        List<ScaffoldingLogPositionDimensionBaseDTO> findByScaffoldingLogPositionId(Long id);
}
