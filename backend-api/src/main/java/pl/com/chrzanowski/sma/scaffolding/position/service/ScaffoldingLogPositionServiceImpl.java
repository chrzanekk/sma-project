package pl.com.chrzanowski.sma.scaffolding.position.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionException;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogPositionErrorCode;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.ScaffoldingLogPositionDimensionService;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.ScaffoldingLogPositionWorkingTimeService;

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

    private final ScaffoldingLogPositionDimensionService scaffoldingLogPositionDimensionService;

    private final ScaffoldingLogPositionWorkingTimeService scaffoldingLogPositionWorkingTimeService;

    public ScaffoldingLogPositionServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper, ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper, ScaffoldingLogPositionDimensionService scaffoldingLogPositionDimensionService, ScaffoldingLogPositionWorkingTimeService scaffoldingLogPositionWorkingTimeService) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionDTOMapper = scaffoldingLogPositionDTOMapper;
        this.scaffoldingLogPositionBaseMapper = scaffoldingLogPositionBaseMapper;
        this.scaffoldingLogPositionDimensionService = scaffoldingLogPositionDimensionService;
        this.scaffoldingLogPositionWorkingTimeService = scaffoldingLogPositionWorkingTimeService;
    }


    @Override
    public ScaffoldingLogPositionDTO save(ScaffoldingLogPositionDTO createDto) {
        log.debug("Request to save ScaffoldingLogPosition : {}", createDto.toString());
        //todo update parent position if exists and calculate full dimension of position.
        if (createDto.getParentPosition() != null) {
            List<ScaffoldingLogPositionDimensionBaseDTO> current = createDto.getParentPosition().getDimensions();
            BigDecimal parentPositionDimension = createDto.getParentPosition().getScaffoldingFullDimension();
            BigDecimal newDimension = calculateScaffoldingDimensions(parentPositionDimension, current);
            ScaffoldingLogPositionBaseDTO parentPosition = createDto.getParentPosition();
            parentPosition = parentPosition.toBuilder().scaffoldingFullDimension(newDimension).build();
            ScaffoldingLogPosition parentPositionEntity = scaffoldingLogPositionBaseMapper.toEntity(parentPosition);
            parentPositionEntity = scaffoldingLogPositionDao.save(parentPositionEntity);
            createDto = createDto.toBuilder().parentPosition(scaffoldingLogPositionBaseMapper.toDto(parentPositionEntity)).build();
        } else {
            BigDecimal dimension = calculateScaffoldingDimensions(BigDecimal.ZERO, createDto.getDimensions());
            createDto = createDto.toBuilder().scaffoldingFullDimension(dimension).build();
        }

        //todo save base position when we updated parent position if exists
        ScaffoldingLogPosition toSaveEntity = scaffoldingLogPositionDTOMapper.toEntity(createDto);
        ScaffoldingLogPosition savedEntity = scaffoldingLogPositionDao.save(toSaveEntity);
        ScaffoldingLogPositionDTO savedDTO = scaffoldingLogPositionDTOMapper.toDto(savedEntity);

        //todo save dimensions (new positionId must be present? and after test if returned saved position dont have dimensions we can add it)
        List<ScaffoldingLogPositionDimensionDTO> savedDimensions = createDto.getDimensions().stream().map(positionDimensionBaseDTO -> {
            ScaffoldingLogPositionDimensionDTO toSave = ScaffoldingLogPositionDimensionDTO.builder()
                    .company(savedDTO.getCompany())
                    .dimensionType(positionDimensionBaseDTO.getDimensionType())
                    .height(positionDimensionBaseDTO.getHeight())
                    .width(positionDimensionBaseDTO.getWidth())
                    .length(positionDimensionBaseDTO.getLength())
                    .operationType(positionDimensionBaseDTO.getOperationType())
                    .dismantlingDate(positionDimensionBaseDTO.getDismantlingDate())
                    .assemblyDate(positionDimensionBaseDTO.getAssemblyDate())
                    .scaffoldingPosition(savedDTO)
                    .build();
            return scaffoldingLogPositionDimensionService.save(toSave);
        }).toList();

        //todo save working times (new positionId must be present? and after test if returned saved position dont have workingTimes we can add it)
        List<ScaffoldingLogPositionWorkingTimeDTO> savedWorkingTimes = createDto.getWorkingTimes().stream().map(positionWorkingTimeDTO -> {
            ScaffoldingLogPositionWorkingTimeDTO toSave = ScaffoldingLogPositionWorkingTimeDTO.builder()
                    .company(savedDTO.getCompany())
                    .operationType(positionWorkingTimeDTO.getOperationType())
                    .numberOfHours(positionWorkingTimeDTO.getNumberOfHours())
                    .numberOfWorkers(positionWorkingTimeDTO.getNumberOfWorkers())
                    .scaffoldingPosition(savedDTO)
                    .build();
            return scaffoldingLogPositionWorkingTimeService.save(toSave);
        }).toList();

        return findById(savedEntity.getId());
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

    private BigDecimal calculateScaffoldingDimensions(BigDecimal existingDimension, List<ScaffoldingLogPositionDimensionBaseDTO> dimensionDTOList) {
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
