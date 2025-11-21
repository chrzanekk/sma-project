package pl.com.chrzanowski.sma.unit.dao;

import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.common.enumeration.UnitType;
import pl.com.chrzanowski.sma.unit.model.Unit;

import java.util.List;
import java.util.Optional;


public interface UnitDao extends BaseCrudDao<Unit, Long> {

    Optional<Unit> findBySymbolAndCompanyId(String symbol, Long companyId);

    List<Unit> findByCompanyIdAndUnitType(Long companyId, UnitType unitType);

    List<Unit> findByCompanyId(Long companyId);
}
