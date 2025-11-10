package pl.com.chrzanowski.sma.scaffolding.dimension.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionDimensionException;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogPositionDimensionErrorCode;
import pl.com.chrzanowski.sma.scaffolding.dimension.dao.ScaffoldingLogPositionDimensionDao;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;

import java.util.Optional;

@Service
@Transactional
public class ScaffoldingLogPositionDimensionServiceImpl implements ScaffoldingLogPositionDimensionService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionDimensionServiceImpl.class);

    private final ScaffoldingLogPositionDimensionDao dao;
    private final ScaffoldingLogPositionDimensionDTOMapper dtoMapper;

    public ScaffoldingLogPositionDimensionServiceImpl(ScaffoldingLogPositionDimensionDao dao, ScaffoldingLogPositionDimensionDTOMapper dtoMapper) {
        this.dao = dao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ScaffoldingLogPositionDimensionDTO save(ScaffoldingLogPositionDimensionDTO createDto) {
        log.debug("Request to save ScaffoldingLogPositionDimension : {}", createDto.toString());
        ScaffoldingLogPositionDimension createEntity = dtoMapper.toEntity(createDto);
        ScaffoldingLogPositionDimension entity = dao.save(createEntity);
        return dtoMapper.toDto(entity);
    }

    @Override
    public ScaffoldingLogPositionDimensionDTO update(ScaffoldingLogPositionDimensionDTO updateDto) {
        log.debug("Request to update ScaffoldingLogPositionDimension : {}", updateDto.getId());
        ScaffoldingLogPositionDimension existing = dao.findById(updateDto.getId())
                .orElseThrow(() -> new ScaffoldingLogPositionDimensionException(ScaffoldingLogPositionDimensionErrorCode.SCAFFOLDING_LOG_POSITION_DIMENSION_NOT_FOUND, "Dimension " + updateDto.getId() + "not found"));
        dtoMapper.updateFromDto(updateDto, existing);
        ScaffoldingLogPositionDimension updateEntity = dao.save(existing);
        return dtoMapper.toDto(updateEntity);
    }

    @Override
    public ScaffoldingLogPositionDimensionDTO findById(Long aLong) {
        log.debug("Request to get Dimension by id: {}", aLong);
        Optional<ScaffoldingLogPositionDimension> result = dao.findById(aLong);
        return dtoMapper.toDto(result.orElseThrow(
                () -> new ScaffoldingLogPositionDimensionException(ScaffoldingLogPositionDimensionErrorCode.SCAFFOLDING_LOG_POSITION_DIMENSION_NOT_FOUND, "Dimension " + aLong + "not found")));

    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete Dimension by id: {}", aLong);
        if (dao.findById(aLong).isEmpty()) {
            throw new ScaffoldingLogPositionDimensionException(ScaffoldingLogPositionDimensionErrorCode.SCAFFOLDING_LOG_POSITION_DIMENSION_NOT_FOUND, "Dimension " + aLong + "not found");
        }
        dao.deleteById(aLong);
    }
}
