package pl.com.chrzanowski.sma.scaffolding.dimension.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;

import java.util.List;

public interface ScaffoldingLogPositionDimensionDao extends BaseCrudDao<ScaffoldingLogPositionDimension, Long> {
    List<ScaffoldingLogPositionDimension> findByScaffoldingLogPositionId(Long id);
}
