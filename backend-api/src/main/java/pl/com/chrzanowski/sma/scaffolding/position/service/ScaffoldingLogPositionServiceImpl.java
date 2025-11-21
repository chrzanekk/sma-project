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
import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.service.UnitService;

import java.math.BigDecimal;
import java.util.*;


@Service
@Transactional
public class ScaffoldingLogPositionServiceImpl implements ScaffoldingLogPositionService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionServiceImpl.class);

    private final ScaffoldingLogPositionDao scaffoldingLogPositionDao;
    private final ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper;
    private final ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper;
    private final ScaffoldingNumberValidationService scaffoldingNumberValidationService;
    private final UnitService unitService;


    public ScaffoldingLogPositionServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper, ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper, ScaffoldingNumberValidationService scaffoldingNumberValidationService, UnitService unitService) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionDTOMapper = scaffoldingLogPositionDTOMapper;
        this.scaffoldingLogPositionBaseMapper = scaffoldingLogPositionBaseMapper;
        this.scaffoldingNumberValidationService = scaffoldingNumberValidationService;
        this.unitService = unitService;
    }


    @Override
    public ScaffoldingLogPositionDTO save(ScaffoldingLogPositionDTO createDto) {
        log.debug("Request to save ScaffoldingLogPosition : {}", createDto.toString());

        scaffoldingNumberValidationService.validateScaffoldingNumber(createDto.getScaffoldingNumber());

        Boolean existingPosition = scaffoldingLogPositionDao.existsByScaffoldingNumberAndScaffoldingLogId(createDto.getScaffoldingNumber(), createDto.getScaffoldingLog().getId());
        if (existingPosition) {
            throw new ScaffoldingLogPositionException(ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_ALREADY_EXIST, "Scaffolding Position with number " + createDto.getScaffoldingNumber() + " already exist");
        }


        if (createDto.getParentPosition() != null) {
            ScaffoldingLogPositionDTO parentPosition = findById(createDto.getParentPosition().getId());
            BigDecimal parentPositionDimension = createDto.getParentPosition().getScaffoldingFullDimension();

            Map<UnitBaseDTO, BigDecimal> parentDimensionResult = calculateScaffoldingDimension(parentPositionDimension, createDto.getDimensions(), createDto.getCompany().getId());

            Map.Entry<UnitBaseDTO, BigDecimal> parentEntry = parentDimensionResult.entrySet().iterator().next();

            parentPosition = parentPosition.toBuilder()
                    .scaffoldingFullDimension(parentEntry.getValue())
                    .scaffoldingFullDimensionUnit(parentEntry.getKey())
                    .build();
            ScaffoldingLogPosition parentPositionEntity = scaffoldingLogPositionBaseMapper.toEntity(parentPosition);
            parentPositionEntity = scaffoldingLogPositionDao.save(parentPositionEntity);
            createDto = createDto.toBuilder().parentPosition(scaffoldingLogPositionBaseMapper.toDto(parentPositionEntity)).build();
        }

        Map<UnitBaseDTO, BigDecimal> calculatedScaffoldingDimension = calculateScaffoldingDimension(BigDecimal.ZERO, createDto.getDimensions(), createDto.getCompany().getId());
        Map.Entry<UnitBaseDTO, BigDecimal> calculatedScaffoldingDimensionEntry = calculatedScaffoldingDimension.entrySet().iterator().next();
        createDto = createDto.toBuilder()
                .scaffoldingFullDimension(calculatedScaffoldingDimensionEntry.getValue())
                .scaffoldingFullDimensionUnit(calculatedScaffoldingDimensionEntry.getKey())
                .build();

        ScaffoldingLogPosition toSaveEntity = scaffoldingLogPositionDTOMapper.toEntity(createDto);

        if (toSaveEntity.getDimensions() != null) {
            toSaveEntity.getDimensions().forEach(dimension -> {
                dimension.setScaffoldingPosition(toSaveEntity);
                dimension.setCompany(toSaveEntity.getCompany());
            });
        }

        if (toSaveEntity.getWorkingTimes() != null && !toSaveEntity.getWorkingTimes().isEmpty()) {
            toSaveEntity.getWorkingTimes().forEach(workingTime -> {
                workingTime.setScaffoldingPosition(toSaveEntity);
                workingTime.setCompany(toSaveEntity.getCompany());
            });
        }
        ScaffoldingLogPosition savedEntity = scaffoldingLogPositionDao.save(toSaveEntity);

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

    private Map<UnitBaseDTO, BigDecimal> calculateScaffoldingDimension(BigDecimal existingDimension,
                                                                       List<ScaffoldingLogPositionDimensionBaseDTO> dimensionDTOList,
                                                                       Long companyId) {
        int runningMetersCount = 0;
        int squareMetersCount = 0;
        int cubicMetersCount = 0;
        int dimensionSize = dimensionDTOList.size();

        BigDecimal calculatedDimension = BigDecimal.ZERO;

        for (ScaffoldingLogPositionDimensionBaseDTO dimensionDTO : dimensionDTOList) {
            BigDecimal value;

            // Określ typ wymiaru i oblicz wartość
            if (dimensionDTO.getWidth().compareTo(BigDecimal.ONE) > 0) {
                // OBJĘTOŚĆ (m3): szerokość > 1
                value = dimensionDTO.getHeight()
                        .multiply(dimensionDTO.getWidth())
                        .multiply(dimensionDTO.getLength());
                cubicMetersCount++;
                log.debug("Dimension type: m3 (width > 1), width: {}", dimensionDTO.getWidth());

            } else if (dimensionDTO.getWidth().compareTo(BigDecimal.ONE) == 0
                    && dimensionDTO.getHeight().compareTo(BigDecimal.ONE) == 0) {
                // DŁUGOŚĆ (mb): szerokość = 1 i wysokość = 1
                value = dimensionDTO.getLength();
                runningMetersCount++;
                log.debug("Dimension type: mb (width = 1 and height = 1)");

            } else {
                // POWIERZCHNIA (m2): szerokość <= 1 (ale nie oba = 1)
                value = dimensionDTO.getHeight()
                        .multiply(dimensionDTO.getLength());
                squareMetersCount++;
                log.debug("Dimension type: m2 (width <= 1, width: {}, height: {})",
                        dimensionDTO.getWidth(), dimensionDTO.getHeight());
            }

            // Dodaj lub odejmij w zależności od typu operacji
            BigDecimal signedValue = dimensionDTO.getOperationType().equals(ScaffoldingOperationType.ASSEMBLY)
                    ? value
                    : value.negate();

            calculatedDimension = calculatedDimension.add(signedValue);
        }
        calculatedDimension = calculatedDimension.add(Objects.requireNonNullElse(existingDimension, BigDecimal.ZERO));

        log.debug("Dimension counts - cubic: {}, square: {}, running: {}, total: {}",
                cubicMetersCount, squareMetersCount, runningMetersCount, dimensionSize);
        String unitSymbol;
        if (cubicMetersCount > 0) {
            unitSymbol = "m3";
            log.debug("Determined unit: m3 (cubicMetersCount > 0)");
        } else if (cubicMetersCount == 0 && (squareMetersCount > 0
                || squareMetersCount == dimensionSize)) {
            unitSymbol = "m2";
            log.debug("Determined unit: m2 (cubicMetersCount == 0 && squareMetersCount > 0 && (squareMetersCount > runningMetersCount || squareMetersCount == dimensionSize))");
        } else if (runningMetersCount == dimensionSize) {
            // Warunek 3: Brak m3, wszystkie wymiary to mb → całość w mb
            unitSymbol = "mb";
            log.debug("Determined unit: mb (cubicMetersCount == 0 && runningMetersCount == dimensionSize)");

        } else {
            // Domyślnie m3 (dla pustej listy lub nieokreślonych przypadków)
            unitSymbol = "m3";
            log.warn("Could not determine unit based on counts, defaulting to m2. " +
                            "Counts: cubic={}, square={}, running={}, total={}",
                    cubicMetersCount, squareMetersCount, runningMetersCount, dimensionSize);
        }

        UnitDTO unitDTO = unitService.findBySymbolAndCompanyId(unitSymbol, companyId);

        Map<UnitBaseDTO, BigDecimal> result = new HashMap<>();
        result.put(unitDTO, calculatedDimension);
        return result;
    }
}
