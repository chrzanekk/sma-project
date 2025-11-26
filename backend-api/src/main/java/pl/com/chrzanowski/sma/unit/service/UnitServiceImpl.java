package pl.com.chrzanowski.sma.unit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.config.CacheConfig;
import pl.com.chrzanowski.sma.common.enumeration.UnitType;
import pl.com.chrzanowski.sma.common.exception.UnitException;
import pl.com.chrzanowski.sma.common.exception.error.UnitErrorCode;
import pl.com.chrzanowski.sma.unit.dao.UnitDao;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitDTOMapper;
import pl.com.chrzanowski.sma.unit.model.Unit;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

    private final UnitDao unitDao;
    private final UnitDTOMapper unitDTOMapper;

    public UnitServiceImpl(UnitDao unitDao, UnitDTOMapper unitDTOMapper) {
        this.unitDao = unitDao;
        this.unitDTOMapper = unitDTOMapper;
    }


    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheConfig.GLOBAL_UNITS_CACHE, allEntries = true, condition = "#result.company == null"),
            @CacheEvict(value = CacheConfig.GLOBAL_UNIT_BY_SYMBOL_CACHE, allEntries = true, condition = "#result.company == null"),
            @CacheEvict(value = CacheConfig.GLOBAL_UNITS_BY_TYPE_CACHE, allEntries = true, condition = "#result.company == null"),
            @CacheEvict(value = CacheConfig.UNIT_BY_ID_CACHE, key = "#result.id", condition = "#result.company == null")
    })
    public UnitDTO save(UnitDTO createDto) {
        log.debug("Request to save Unit : {}", createDto.getSymbol());
        Unit unit = unitDTOMapper.toEntity(createDto);
        Unit saved = unitDao.save(unit);
        return unitDTOMapper.toDto(saved);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConfig.GLOBAL_UNITS_CACHE, allEntries = true),
            @CacheEvict(value = CacheConfig.GLOBAL_UNIT_BY_SYMBOL_CACHE, allEntries = true),
            @CacheEvict(value = CacheConfig.GLOBAL_UNITS_BY_TYPE_CACHE, allEntries = true),
            @CacheEvict(value = CacheConfig.UNIT_BY_ID_CACHE, key = "#id")
    })
    public UnitDTO update(UnitDTO updateDto) {
        log.debug("Request to update Unit : {}", updateDto.getId());
        Unit existingUnit = unitDao.findById(updateDto.getId()).orElseThrow(() -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND, "Unit with id " + updateDto.getId() + " not found"));

        if (existingUnit.getCompany() == null) {
            throw new UnitException(
                    UnitErrorCode.GLOBAL_UNIT_CANNOT_BE_MODIFIED,
                    "Global unit with id " + updateDto.getId() + " cannot be updated. Global units are read-only."
            );
        }

        unitDTOMapper.updateFromDto(updateDto, existingUnit);
        Unit updatedPosition = unitDao.save(existingUnit);
        return unitDTOMapper.toDto(updatedPosition);
    }

    @Override
    @Cacheable(value = CacheConfig.UNIT_BY_ID_CACHE, key = "#id",
            condition = "#id != null")
    public UnitDTO findById(Long aLong) {
        log.debug("Request to get Unit : {}", aLong);
        Optional<Unit> unit = unitDao.findById(aLong);
        return unitDTOMapper.toDto(unit.orElseThrow(() -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND, "Unit with id " + aLong + " not found")));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConfig.GLOBAL_UNITS_CACHE, allEntries = true),
            @CacheEvict(value = CacheConfig.GLOBAL_UNIT_BY_SYMBOL_CACHE, allEntries = true),
            @CacheEvict(value = CacheConfig.GLOBAL_UNITS_BY_TYPE_CACHE, allEntries = true),
            @CacheEvict(value = CacheConfig.UNIT_BY_ID_CACHE, key = "#id")
    })
    public void delete(Long id) {
        log.debug("Request to delete Unit : {}", id);
        Unit existingUnit = unitDao.findById(id)
                .orElseThrow(() -> new UnitException(
                        UnitErrorCode.UNIT_NOT_FOUND,
                        "Unit with id " + id + " not found"
                ));

        if (existingUnit.getCompany() == null) {
            throw new UnitException(
                    UnitErrorCode.GLOBAL_UNIT_CANNOT_BE_DELETED,
                    "Global unit with id " + id + " cannot be deleted. Global units are system-wide and read-only."
            );
        }

        unitDao.deleteById(id);
        log.debug("Unit deleted, cache evicted");
    }

    @Override
    public UnitDTO findBySymbolAndCompanyId(String symbol, Long companyId) {
        log.debug("Request to get Unit by symbol : {}", symbol);
        Optional<Unit> unit = unitDao.findBySymbolAndCompanyId(symbol, companyId);
        return unitDTOMapper.toDto(unit.orElseThrow(
                () -> new UnitException(
                        UnitErrorCode.UNIT_NOT_FOUND,
                        "Unit with symbol " + symbol + " not found"
                )));
    }

    @Override
    public List<UnitDTO> findByCompanyIdAndUnitType(Long companyId, UnitType unitType) {
        log.debug("Request to get Units by companyId and unitType : {},{}", companyId, unitType);
        return unitDTOMapper.toDtoList(unitDao.findByCompanyIdAndUnitType(companyId, unitType));
    }

    @Override
    public List<UnitDTO> findByCompanyId(Long companyId) {
        log.debug("Request to get Units by companyId: {}", companyId);
        return unitDTOMapper.toDtoList(unitDao.findByCompanyId(companyId));
    }

    @Override
    @Cacheable(value = CacheConfig.GLOBAL_UNITS_CACHE)
    public List<UnitDTO> findAllGlobalUnits() {
        log.debug("Request to get all global units (cacheable)");
        return unitDTOMapper.toDtoList(unitDao.findAllGlobalUnits());
    }

    @Override
    @Cacheable(value = CacheConfig.GLOBAL_UNIT_BY_SYMBOL_CACHE, key = "#symbol")
    public UnitDTO findGlobalUnitBySymbol(String symbol) {
        log.debug("Request to get global unit by symbol: {} (cacheable)", symbol);
        Optional<Unit> unit = unitDao.findGlobalUnitBySymbol(symbol);
        return unitDTOMapper.toDto(unit.orElseThrow(
                () -> new UnitException(
                        UnitErrorCode.UNIT_NOT_FOUND,
                        "Global unit with symbol " + symbol + " not found"
                )
        ));
    }

    @Override
    @Cacheable(value = CacheConfig.GLOBAL_UNITS_BY_TYPE_CACHE, key = "#unitType")
    public List<UnitDTO> findGlobalUnitsByType(UnitType unitType) {
        log.debug("Request to get global units by type: {} (cacheable)", unitType);
        return unitDTOMapper.toDtoList(unitDao.findGlobalUnitsByType(unitType));
    }
}
