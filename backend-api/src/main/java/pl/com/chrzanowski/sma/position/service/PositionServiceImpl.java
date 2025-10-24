package pl.com.chrzanowski.sma.position.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.PositionException;
import pl.com.chrzanowski.sma.common.exception.error.PositionErrorCode;
import pl.com.chrzanowski.sma.position.dao.PositionDao;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.mapper.PositionDTOMapper;
import pl.com.chrzanowski.sma.position.model.Position;

import java.util.Optional;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private final Logger log = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionDao positionDao;
    private final PositionDTOMapper positionDTOMapper;

    public PositionServiceImpl(PositionDao positionDao, PositionDTOMapper positionDTOMapper) {
        this.positionDao = positionDao;
        this.positionDTOMapper = positionDTOMapper;
    }

    @Override
    @Transactional
    public PositionDTO save(PositionDTO createDto) {
        log.debug("Request to save PositionBase : {}", createDto.getName());
        Position position = positionDTOMapper.toEntity(createDto);
        Position savedPosition = positionDao.save(position);
        return positionDTOMapper.toDto(savedPosition);
    }

    @Override
    public PositionDTO update(PositionDTO updateDto) {
        log.debug("Request to update PositionBase : {}", updateDto.getId());
        Position existingPosition = positionDao.findById(updateDto.getId()).orElseThrow(() -> new PositionException(PositionErrorCode.POSITION_NOT_FOUND, "Position with id " + updateDto.getId() + " not found"));
        positionDTOMapper.updateFromDto(updateDto, existingPosition);
        Position updatedPosition = positionDao.save(existingPosition);
        return positionDTOMapper.toDto(updatedPosition);
    }

    @Override
    public PositionDTO findById(Long aLong) {
        log.debug("Request to get PositionBase : {}", aLong);
        Optional<Position> position = positionDao.findById(aLong);
        return positionDTOMapper.toDto(position.orElseThrow(() -> new PositionException(PositionErrorCode.POSITION_NOT_FOUND, "Position with id " + aLong + " not found")));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete Position : {}", aLong);
        if (!positionDao.findById(aLong).isPresent()) {
            throw new PositionException(
                    PositionErrorCode.POSITION_NOT_FOUND,
                    "Position with id " + aLong + " not found"
            );
        }
        positionDao.deleteById(aLong);
    }
}
