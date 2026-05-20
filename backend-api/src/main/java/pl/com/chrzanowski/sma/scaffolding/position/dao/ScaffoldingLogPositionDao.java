package pl.com.chrzanowski.sma.scaffolding.position.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

import java.util.List;
import java.util.Optional;

public interface ScaffoldingLogPositionDao extends BaseCrudDao<ScaffoldingLogPosition, Long> {

    Boolean existsByScaffoldingNumberAndScaffoldingLogId(String scaffoldingNumber, Long scaffoldingLogId);

    Boolean existsById(Long id);

    List<ScaffoldingLogPosition> findFamilyPositions(Long rootId);

    Optional<ScaffoldingLogPosition> findLastParentPositionInLog(Long logId, int year);
}
