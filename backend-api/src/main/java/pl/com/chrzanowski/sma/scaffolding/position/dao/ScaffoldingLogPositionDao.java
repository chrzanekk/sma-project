package pl.com.chrzanowski.sma.scaffolding.position.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

public interface ScaffoldingLogPositionDao extends BaseCrudDao<ScaffoldingLogPosition, Long> {

    Boolean existsByScaffoldingNumberAndScaffoldingLogId(String scaffoldingNumber, Long scaffoldingLogId);
    Boolean existsById(Long id);
}
