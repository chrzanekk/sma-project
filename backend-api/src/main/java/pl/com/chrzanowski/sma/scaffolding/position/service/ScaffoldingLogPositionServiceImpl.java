package pl.com.chrzanowski.sma.scaffolding.position.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionException;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogPositionErrorCode;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.validator.ScaffoldingNumberValidationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class ScaffoldingLogPositionServiceImpl implements ScaffoldingLogPositionService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionServiceImpl.class);

    private final ScaffoldingLogPositionDao scaffoldingLogPositionDao;
    private final ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper;
    private final ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper;
    private final ScaffoldingNumberValidationService scaffoldingNumberValidationService;


    public ScaffoldingLogPositionServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper, ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper, ScaffoldingNumberValidationService scaffoldingNumberValidationService) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionDTOMapper = scaffoldingLogPositionDTOMapper;
        this.scaffoldingLogPositionBaseMapper = scaffoldingLogPositionBaseMapper;
        this.scaffoldingNumberValidationService = scaffoldingNumberValidationService;
    }


    @Override
    public ScaffoldingLogPositionDTO save(ScaffoldingLogPositionDTO createDto) {
        log.debug("Request to save ScaffoldingLogPosition : {}", createDto.toString());

        scaffoldingNumberValidationService.validateScaffoldingNumber(createDto.getScaffoldingNumber());

        Boolean existingPosition = scaffoldingLogPositionDao.existsByScaffoldingNumberAndScaffoldingLogId(createDto.getScaffoldingNumber(), createDto.getScaffoldingLog().getId());
        if (existingPosition) {
            throw new ScaffoldingLogPositionException(ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_ALREADY_EXIST, "Scaffolding Position with number " + createDto.getScaffoldingNumber() + " already exist");
        } else {
            if (createDto.getParentPosition() != null) {
                ScaffoldingLogPositionDTO parentPosition = findById(createDto.getParentPosition().getId());
                BigDecimal parentPositionDimension = createDto.getParentPosition().getScaffoldingFullDimension();

                BigDecimal newDimension = calculateScaffoldingDimension(parentPositionDimension, createDto.getDimensions());

                parentPosition = parentPosition.toBuilder().scaffoldingFullDimension(newDimension).build();
                ScaffoldingLogPosition parentPositionEntity = scaffoldingLogPositionBaseMapper.toEntity(parentPosition);
                parentPositionEntity = scaffoldingLogPositionDao.save(parentPositionEntity);
                createDto = createDto.toBuilder().parentPosition(scaffoldingLogPositionBaseMapper.toDto(parentPositionEntity)).build();
            }

            BigDecimal calculateScaffoldingDimension = calculateScaffoldingDimension(BigDecimal.ZERO, createDto.getDimensions());
            createDto = createDto.toBuilder().scaffoldingFullDimension(calculateScaffoldingDimension).build();


            ScaffoldingLogPosition toSaveEntity = scaffoldingLogPositionDTOMapper.toEntity(createDto);

            if (toSaveEntity.getDimensions() != null) {
                toSaveEntity.getDimensions().forEach(dimension -> {
                    dimension.setScaffoldingPosition(toSaveEntity);
                    dimension.setCompany(toSaveEntity.getCompany());
                });
            }

            if (toSaveEntity.getWorkingTimes() != null) {
                toSaveEntity.getWorkingTimes().forEach(workingTime -> {
                    workingTime.setScaffoldingPosition(toSaveEntity);
                    workingTime.setCompany(toSaveEntity.getCompany());
                });
            }

            ScaffoldingLogPosition savedEntity = scaffoldingLogPositionDao.save(toSaveEntity);

            return findById(savedEntity.getId());
        }
    }

    @Override
    public ScaffoldingLogPositionDTO update(ScaffoldingLogPositionDTO updateDto) {
        return null;
    }

    @Override
    public ScaffoldingLogPositionDTO findById(Long aLong) {
        log.debug("Request to get ScaffoldingLogPosition by id: {}", aLong);
        Optional<ScaffoldingLogPosition> result = scaffoldingLogPositionDao.findById(aLong);
        return scaffoldingLogPositionDTOMapper.toDto(result.orElseThrow(
                () -> new ScaffoldingLogPositionException(ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND, "Scaffolding Log Position: " + aLong + " not found")
        ));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete ScaffoldingLogPosition by id: {}", aLong);
        if (scaffoldingLogPositionDao.findById(aLong).isEmpty()) {
            throw new ScaffoldingLogPositionException(ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND, "Scaffolding Log Position: " + aLong + " not found");
        }
        scaffoldingLogPositionDao.deleteById(aLong);
    }

    private BigDecimal calculateScaffoldingDimension(BigDecimal existingDimension, List<ScaffoldingLogPositionDimensionBaseDTO> dimensionDTOList) {
        BigDecimal calculatedDimension = dimensionDTOList.stream()
                .map(dimensionDTO -> {
                    BigDecimal value = dimensionDTO.getHeight()
                            .multiply(dimensionDTO.getWidth())
                            .multiply(dimensionDTO.getLength());
                    return dimensionDTO.getOperationType().equals(ScaffoldingOperationType.ASSEMBLY) ? value : value.negate();
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
        return calculatedDimension.add(Objects.requireNonNullElse(existingDimension, BigDecimal.ZERO));
    }
}
