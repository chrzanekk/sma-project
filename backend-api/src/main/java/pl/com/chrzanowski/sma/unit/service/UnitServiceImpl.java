package pl.com.chrzanowski.sma.unit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.UnitException;
import pl.com.chrzanowski.sma.common.exception.error.UnitErrorCode;
import pl.com.chrzanowski.sma.unit.dao.UnitDao;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitDTOMapper;
import pl.com.chrzanowski.sma.unit.model.Unit;

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
    public UnitDTO save(UnitDTO createDto) {
        log.debug("Request to save PositionBase : {}", createDto.getSymbol());
        Unit position = unitDTOMapper.toEntity(createDto);
        Unit savedPosition = unitDao.save(position);
        return unitDTOMapper.toDto(savedPosition);
    }

    @Override
    public UnitDTO update(UnitDTO updateDto) {
        log.debug("Request to update PositionBase : {}", updateDto.getId());
        Unit existingUnit = unitDao.findById(updateDto.getId()).orElseThrow(() -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND, "Unit with id " + updateDto.getId() + " not found"));
        unitDTOMapper.updateFromDto(updateDto, existingUnit);
        Unit updatedPosition = unitDao.save(existingUnit);
        return unitDTOMapper.toDto(updatedPosition);
    }

    @Override
    public UnitDTO findById(Long aLong) {
        log.debug("Request to get PositionBase : {}", aLong);
        Optional<Unit> unit = unitDao.findById(aLong);
        return unitDTOMapper.toDto(unit.orElseThrow(() -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND, "Unit with id " + aLong + " not found")));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete Position : {}", aLong);
        if (!unitDao.findById(aLong).isPresent()) {
            throw new UnitException(
                    UnitErrorCode.UNIT_NOT_FOUND,
                    "Unit with id " + aLong + " not found"
            );
        }
        unitDao.deleteById(aLong);
    }
}
