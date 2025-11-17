package pl.com.chrzanowski.sma.scaffolding.position.service;

import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;

import java.util.Optional;

public interface ScaffoldingLogPositionService extends BaseCrudService<
        ScaffoldingLogPositionDTO,
        ScaffoldingLogPositionDTO,
        ScaffoldingLogPositionDTO,
        Long> {
}
