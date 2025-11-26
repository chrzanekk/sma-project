package pl.com.chrzanowski.sma.unit.service;

import pl.com.chrzanowski.sma.common.enumeration.UnitType;
import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;

import java.util.List;

public interface UnitService extends BaseCrudService<UnitDTO, UnitDTO, UnitDTO, Long> {

    UnitDTO findBySymbolAndCompanyId(String symbol, Long companyId);

    List<UnitDTO> findByCompanyIdAndUnitType(Long companyId, UnitType unitType);

    List<UnitDTO> findByCompanyId(Long companyId);

    List<UnitDTO> findAllGlobalUnits();
    UnitDTO findGlobalUnitBySymbol(String symbol);
    List<UnitDTO> findGlobalUnitsByType(UnitType unitType);
}
