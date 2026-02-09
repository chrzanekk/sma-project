package pl.com.chrzanowski.sma.scaffolding.position.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.enumeration.DimensionType;
import pl.com.chrzanowski.sma.common.enumeration.ScaffoldingOperationType;
import pl.com.chrzanowski.sma.common.exception.ContractorException;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionException;
import pl.com.chrzanowski.sma.common.exception.error.ContractorErrorCode;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogPositionErrorCode;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.service.ContactService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.DimensionResult;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.validator.ScaffoldingNumberValidationService;
import pl.com.chrzanowski.sma.scaffolding.workingtime.dto.ScaffoldingLogPositionWorkingTimeBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitBaseMapper;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.unit.service.UnitService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class ScaffoldingLogPositionServiceImpl implements ScaffoldingLogPositionService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingLogPositionServiceImpl.class);

    private final ScaffoldingLogPositionDao scaffoldingLogPositionDao;
    private final ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper;
    private final ScaffoldingNumberValidationService scaffoldingNumberValidationService;
    private final UnitService unitService;
    private final UnitBaseMapper unitBaseMapper;
    private final ContractorService contractorService;
    private final ContractorBaseMapper contractorBaseMapper;
    private final ContactService contactService;
    private final ContactBaseMapper contactBaseMapper;
    private final ScaffoldingLogPositionDimensionBaseMapper scaffoldingLogPositionDimensionBaseMapper;
    private final ScaffoldingLogPositionWorkingTimeBaseMapper scaffoldingLogPositionWorkingTimeBaseMapper;

    public ScaffoldingLogPositionServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper, ScaffoldingNumberValidationService scaffoldingNumberValidationService, UnitService unitService, UnitBaseMapper unitBaseMapper, ContractorService contractorService, ContractorBaseMapper contractorBaseMapper, ContactService contactService, ContactBaseMapper contactBaseMapper, ScaffoldingLogPositionDimensionBaseMapper scaffoldingLogPositionDimensionBaseMapper, ScaffoldingLogPositionWorkingTimeBaseMapper scaffoldingLogPositionWorkingTimeBaseMapper) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionDTOMapper = scaffoldingLogPositionDTOMapper;
        this.scaffoldingNumberValidationService = scaffoldingNumberValidationService;
        this.unitService = unitService;
        this.unitBaseMapper = unitBaseMapper;
        this.contractorService = contractorService;
        this.contractorBaseMapper = contractorBaseMapper;
        this.contactService = contactService;
        this.contactBaseMapper = contactBaseMapper;
        this.scaffoldingLogPositionDimensionBaseMapper = scaffoldingLogPositionDimensionBaseMapper;
        this.scaffoldingLogPositionWorkingTimeBaseMapper = scaffoldingLogPositionWorkingTimeBaseMapper;
    }


    @Override
    public ScaffoldingLogPositionDTO save(ScaffoldingLogPositionDTO createDto) {
        log.debug("Request to save ScaffoldingLogPosition : {}", createDto);

        scaffoldingNumberValidationService.validateScaffoldingNumber(createDto.getScaffoldingNumber());
        checkIfPositionWithNumberExistsInLog(createDto.getScaffoldingNumber(), createDto.getScaffoldingLog().getId());

        // Obliczanie wymiarów dla nowej pozycji
        DimensionResult dimensionResult = calculateScaffoldingDimension(BigDecimal.ZERO, createDto.getDimensions());
        BigDecimal fullWorkingTime = calculateFullWorkingTime(BigDecimal.ZERO, createDto.getWorkingTimes());


        // Mapowanie DTO -> Entity z ustawionymi wymiarami
        ScaffoldingLogPosition toSaveEntity = scaffoldingLogPositionDTOMapper.toEntity(createDto);
        toSaveEntity.setScaffoldingFullDimension(dimensionResult.value());
        toSaveEntity.setScaffoldingFullDimensionUnit(unitBaseMapper.toEntity(dimensionResult.unit()));
        toSaveEntity.setFullWorkingTime(fullWorkingTime);

        // Obsługa Parent Position
        if (createDto.getParentPosition() != null && createDto.getParentPosition().getId() != null) {
            handleParentPositionOnCreate(toSaveEntity, createDto);
        }

        // Ustawienie relacji dwukierunkowych
        linkSubEntities(toSaveEntity);

        ScaffoldingLogPosition savedEntity = scaffoldingLogPositionDao.save(toSaveEntity);
        // OPTYMALIZACJA: Konwersja savedEntity bezpośrednio, bez ponownego findById
        return scaffoldingLogPositionDTOMapper.toDto(savedEntity);
    }

    @Override
    public ScaffoldingLogPositionDTO update(ScaffoldingLogPositionDTO updateDto) {
        log.debug("Request to update ScaffoldingLogPosition : {}", updateDto.getId());

        scaffoldingNumberValidationService.validateScaffoldingNumber(updateDto.getScaffoldingNumber());

        ScaffoldingLogPosition existingPosition = scaffoldingLogPositionDao.findById(updateDto.getId())
                .orElseThrow(() -> new ScaffoldingLogPositionException(
                        ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND,
                        "Scaffolding Log Position with id " + updateDto.getId() + " not found"));

        // Aktualizacja pól prostych z DTO
        scaffoldingLogPositionDTOMapper.updateFromDto(updateDto, existingPosition);

        updateParentRelationship(updateDto, existingPosition);
        updateRelatedEntities(updateDto, existingPosition);

        //check dimension change and recalculate if needed
        // Obsługa zmian wymiarów
        List<ScaffoldingLogPositionDimensionBaseDTO> updatingDimensions = updateDto.getDimensions();
        boolean dimensionChanged = hasDimensionChanged(existingPosition, updateDto.getDimensions());


        if (dimensionChanged) {
            // Aktualizacja kolekcji wymiarów
            mergeDimensions(existingPosition, updatingDimensions);

            // Przeliczenie wymiarów dla bieżącej pozycji
            DimensionResult newDimensions = calculateScaffoldingDimension(BigDecimal.ZERO, updatingDimensions);
            existingPosition.setScaffoldingFullDimension(newDimensions.value());
            existingPosition.setScaffoldingFullDimensionUnit(unitBaseMapper.toEntity(newDimensions.unit()));
        }

        List<ScaffoldingLogPositionWorkingTimeBaseDTO> updatingWorkingTimes = updateDto.getWorkingTimes();
        boolean workingTimeChanged = hasWorkingTimeChanged(existingPosition, updatingWorkingTimes);
        if (workingTimeChanged) {
            mergeWorkingTimes(existingPosition, updatingWorkingTimes);
            BigDecimal fullWorkingTime = calculateFullWorkingTime(BigDecimal.ZERO, updatingWorkingTimes);
            existingPosition.setFullWorkingTime(fullWorkingTime);
        }

        if (existingPosition.getParentPosition() != null && (dimensionChanged || workingTimeChanged)) {
            ScaffoldingLogPosition parentEntity = existingPosition.getParentPosition();
            if (dimensionChanged) {
                updateParentDimensionsState(parentEntity, updatingDimensions);
            }
            if (workingTimeChanged) {
                updateParentWorkingTimeState(parentEntity, updatingWorkingTimes);
            }
        }
        scaffoldingLogPositionDao.save(existingPosition);

        ScaffoldingLogPosition updatedPosition = scaffoldingLogPositionDao.save(existingPosition);
        return findById(updatedPosition.getId());
    }


    private BigDecimal calculateFullWorkingTime(BigDecimal existingWorkingTime, List<ScaffoldingLogPositionWorkingTimeBaseDTO> workingTimes) {
        BigDecimal base = existingWorkingTime != null ? existingWorkingTime : BigDecimal.ZERO;
        if (workingTimes == null || workingTimes.isEmpty()) {
            return base;
        }
        BigDecimal manHours = workingTimes.stream()
                .filter(dto -> dto.getNumberOfWorkers() != null && dto.getNumberOfHours() != null)
                .map(dto -> dto.getNumberOfHours()
                        .multiply(BigDecimal.valueOf(dto.getNumberOfWorkers())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return base.add(manHours);
    }


    private void handleParentPositionOnCreate(ScaffoldingLogPosition childEntity, ScaffoldingLogPositionDTO childDto) {
        ScaffoldingLogPosition parentEntity = scaffoldingLogPositionDao.findById(childDto.getParentPosition().getId())
                .orElseThrow(() -> new ScaffoldingLogPositionException(
                        ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND,
                        "Parent position not found"));

        childEntity.setParentPosition(parentEntity);

        updateParentDimensionsState(parentEntity, childDto.getDimensions());
        updateParentWorkingTimeState(parentEntity, childDto.getWorkingTimes());
        scaffoldingLogPositionDao.save(parentEntity);
        // todo change scaffolding number in child position
    }

    private void updateParentDimensionsState(ScaffoldingLogPosition parentEntity, List<ScaffoldingLogPositionDimensionBaseDTO> childDimensions) {
        DimensionResult parentResult = calculateScaffoldingDimension(parentEntity.getScaffoldingFullDimension(), childDimensions);
        parentEntity.setScaffoldingFullDimension(parentResult.value());
        parentEntity.setScaffoldingFullDimensionUnit(unitBaseMapper.toEntity(parentResult.unit()));
    }

    private void updateParentWorkingTimeState(ScaffoldingLogPosition parentEntity, List<ScaffoldingLogPositionWorkingTimeBaseDTO> childWorkingTimes) {
        BigDecimal fullWorkingTime = calculateFullWorkingTime(parentEntity.getFullWorkingTime(), childWorkingTimes);
        parentEntity.setFullWorkingTime(fullWorkingTime);
    }

    private void checkIfPositionWithNumberExistsInLog(String scaffoldingNumber, Long logId) {
        if (scaffoldingLogPositionDao.existsByScaffoldingNumberAndScaffoldingLogId(scaffoldingNumber, logId)) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_ALREADY_EXIST,
                    "Scaffolding Position with number " + scaffoldingNumber + " already exist");
        }
    }

    private void linkSubEntities(ScaffoldingLogPosition entity) {
        Function<String, Unit> getUnitEntity = getStringUnitFunction();


        if (entity.getDimensions() != null) {
            entity.getDimensions().forEach(d -> {
                d.setScaffoldingPosition(entity);
                d.setCompany(entity.getCompany());
                if (d.getUnit() == null) {
                    ScaffoldingLogPositionDimensionBaseDTO tempDto = ScaffoldingLogPositionDimensionBaseDTO.builder()
                            .width(d.getWidth())
                            .height(d.getHeight())
                            .build();

                    String symbol = determineUnitSymbol(tempDto);
                    d.setUnit(getUnitEntity.apply(symbol));
                }
            });
        }
        if (entity.getWorkingTimes() != null) {
            entity.getWorkingTimes().forEach(wt -> {
                wt.setScaffoldingPosition(entity);
                wt.setCompany(entity.getCompany());
                if (wt.getUnit() == null) {
                    wt.setUnit(getUnitEntity.apply("r-h"));
                }
            });
        }
    }

    private Function<String, Unit> getStringUnitFunction() {
        Map<String, UnitBaseDTO> unitCache = new HashMap<>();

        Function<String, Unit> getUnitEntity = (symbol) -> {
            if (!unitCache.containsKey(symbol)) {
                unitCache.put(symbol, unitService.findGlobalUnitBySymbol(symbol));
            }
            return unitBaseMapper.toEntity(unitCache.get(symbol));
        };
        return getUnitEntity;
    }

    private void updateRelatedEntities(ScaffoldingLogPositionDTO dto, ScaffoldingLogPosition entity) {
        updateContractorIfNeeded(dto.getContractor().getId(), entity);
        updateContractorContactIfNeeded(dto.getContractorContact().getId(), entity);
        updateScaffoldingUserIfNeeded(dto.getScaffoldingUser().getId(), entity);
        updateScaffoldingUserContactIfNeeded(dto.getScaffoldingUserContact().getId(), entity);
    }

    private void updateContractorIfNeeded(Long newContractorId, ScaffoldingLogPosition entity) {
        if (!entity.getContractor().getId().equals(newContractorId)) {
            ContractorBaseDTO dto = contractorService.findById(newContractorId);
            validateContractorIsCustomer(dto);
            entity.setContractor(contractorBaseMapper.toEntity(dto));
        }
    }

    private void updateScaffoldingUserIfNeeded(Long newUserId, ScaffoldingLogPosition entity) {
        if (!entity.getScaffoldingUser().getId().equals(newUserId)) {
            ContractorBaseDTO dto = contractorService.findById(newUserId);
            validateContractorIsCustomer(dto);
            if (!dto.getScaffoldingUser()) {
                throw new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_SCAFFOLDING_USER, "Contractor is not a scaffolding user");
            }
            entity.setScaffoldingUser(contractorBaseMapper.toEntity(dto));
        }
    }

    private void validateContractorIsCustomer(ContractorBaseDTO dto) {
        if (!dto.getCustomer()) {
            throw new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_CUSTOMER, "Contractor is not a customer");
        }
    }

    private void updateContractorContactIfNeeded(Long newContactId, ScaffoldingLogPosition entity) {
        if (!entity.getContractorContact().getId().equals(newContactId)) {
            ContactBaseDTO dto = contactService.findById(newContactId);
            entity.setContractorContact(contactBaseMapper.toEntity(dto));
        }
    }

    private void updateScaffoldingUserContactIfNeeded(Long newContactId, ScaffoldingLogPosition entity) {
        if (!entity.getScaffoldingUserContact().getId().equals(newContactId)) {
            ContactBaseDTO dto = contactService.findById(newContactId);
            entity.setScaffoldingUserContact(contactBaseMapper.toEntity(dto));
        }
    }

    private void updateParentRelationship(ScaffoldingLogPositionDTO dto, ScaffoldingLogPosition entity) {
        Long newParentId = dto.getParentPosition() != null ? dto.getParentPosition().getId() : null;
        Long currentParentId = entity.getParentPosition() != null ? entity.getParentPosition().getId() : null;

        if (!Objects.equals(currentParentId, newParentId)) {
            if (newParentId != null) {
                ScaffoldingLogPosition newParent = scaffoldingLogPositionDao.findById(newParentId)
                        .orElseThrow(() -> new ScaffoldingLogPositionException(
                                ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND,
                                "Parent position not found"));
                entity.setParentPosition(newParent);
            } else {
                entity.setParentPosition(null);
            }
        }
    }

    private void mergeDimensions(ScaffoldingLogPosition existingPosition,
                                 List<ScaffoldingLogPositionDimensionBaseDTO> updatingDTOs) {

        // Mapowanie po ID dla szybkiego dostępu
        Map<Long, ScaffoldingLogPositionDimensionBaseDTO> dtoMap = updatingDTOs.stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(ScaffoldingLogPositionDimensionBaseDTO::getId, Function.identity()));

        Iterator<ScaffoldingLogPositionDimension> iterator = existingPosition.getDimensions().iterator();

        // 1. Usuwanie
        while (iterator.hasNext()) {
            ScaffoldingLogPositionDimension current = iterator.next();
            if (!dtoMap.containsKey(current.getId())) {
                iterator.remove(); // Hibernate orphanRemoval usunie rekord
                current.setScaffoldingPosition(null);
                current.setCompany(null);
            }
        }

        // 2. Aktualizacja i Dodawanie
        for (ScaffoldingLogPositionDimensionBaseDTO dto : updatingDTOs) {
            if (dto.getId() == null) {
                // Dodawanie nowego
                createAndAddNewDimension(dto, existingPosition);
            } else {
                // Aktualizacja istniejącego
                existingPosition.getDimensions().stream()
                        .filter(d -> d.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(existingDim -> updateDimensionFields(existingDim, dto));
            }
        }
    }

    private void mergeWorkingTimes(ScaffoldingLogPosition existingPosition,
                                   List<ScaffoldingLogPositionWorkingTimeBaseDTO> updatingDTOs) {
        Map<Long, ScaffoldingLogPositionWorkingTimeBaseDTO> dtoMap = updatingDTOs
                .stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(ScaffoldingLogPositionWorkingTimeBaseDTO::getId, Function.identity()));

        Iterator<ScaffoldingLogPositionWorkingTime> iterator = existingPosition.getWorkingTimes().iterator();
        while (iterator.hasNext()) {
            ScaffoldingLogPositionWorkingTime current = iterator.next();
            if (!dtoMap.containsKey(current.getId())) {
                iterator.remove();
                current.setScaffoldingPosition(null);
                current.setCompany(null);
            }
        }
        for (ScaffoldingLogPositionWorkingTimeBaseDTO dto : updatingDTOs) {
            if (dto.getId() == null) {
                createAndAddNewWorkingTime(dto, existingPosition);
            } else {
                existingPosition.getWorkingTimes().stream()
                        .filter(d -> d.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(existingWorkingTime -> updateWorkingFields(existingWorkingTime, dto));
            }
        }
    }

    private void createAndAddNewDimension(ScaffoldingLogPositionDimensionBaseDTO dto, ScaffoldingLogPosition position) {
        UnitDTO unitDTO;
        if (dto.getUnit() != null && dto.getUnit().getId() != null) {
            unitDTO = unitService.findById(dto.getUnit().getId());
        } else {
            String symbol = determineUnitSymbol(dto);
            unitDTO = unitService.findGlobalUnitBySymbol(symbol);
        }

        ScaffoldingLogPositionDimension newDimension = ScaffoldingLogPositionDimension.builder()
                .height(dto.getHeight())
                .width(dto.getWidth())
                .length(dto.getLength())
                .dimensionType(dto.getDimensionType())
                .dismantlingDate(dto.getDismantlingDate())
                .assemblyDate(dto.getAssemblyDate())
                .operationType(dto.getOperationType())
                .unit(unitBaseMapper.toEntity(unitDTO))
                .scaffoldingPosition(position)
                .company(position.getCompany())
                .build();

        position.getDimensions().add(newDimension);
    }

    private void createAndAddNewWorkingTime(ScaffoldingLogPositionWorkingTimeBaseDTO dto, ScaffoldingLogPosition position) {
        Function<String, Unit> unitMap = getStringUnitFunction();
        ScaffoldingLogPositionWorkingTime newWorkingTime = ScaffoldingLogPositionWorkingTime.builder()
                .numberOfHours(dto.getNumberOfHours())
                .numberOfWorkers(dto.getNumberOfWorkers())
                .unit(unitMap.apply("r-h"))
                .scaffoldingPosition(position)
                .operationType(dto.getOperationType())
                .company(position.getCompany())
                .build();
        position.getWorkingTimes().add(newWorkingTime);
    }

    private void updateDimensionFields(ScaffoldingLogPositionDimension entity, ScaffoldingLogPositionDimensionBaseDTO dto) {
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setLength(dto.getLength());
        entity.setDimensionType(dto.getDimensionType());
        entity.setDismantlingDate(dto.getDismantlingDate());
        entity.setAssemblyDate(dto.getAssemblyDate());
        entity.setOperationType(dto.getOperationType());

        if (dto.getUnit() != null && dto.getUnit().getId() != null) {
            if (!entity.getUnit().getId().equals(dto.getUnit().getId())) {
                UnitDTO unitDTO = unitService.findById(dto.getUnit().getId());
                entity.setUnit(unitBaseMapper.toEntity(unitDTO));
            }
        }
    }

    private void updateWorkingFields(ScaffoldingLogPositionWorkingTime entity, ScaffoldingLogPositionWorkingTimeBaseDTO dto) {
        entity.setNumberOfHours(dto.getNumberOfHours());
        entity.setNumberOfWorkers(dto.getNumberOfWorkers());
        entity.setUnit(unitBaseMapper.toEntity(dto.getUnit()));
        entity.setOperationType(dto.getOperationType());

    }

    private DimensionResult calculateScaffoldingDimension(BigDecimal existingDimension,
                                                          List<ScaffoldingLogPositionDimensionBaseDTO> dimensions) {
        int cubicMetersCount = 0;
        int squareMetersCount = 0;
        int runningMetersCount = 0;
        BigDecimal calculatedDimension = BigDecimal.ZERO;

        for (ScaffoldingLogPositionDimensionBaseDTO dim : dimensions) {
            BigDecimal value;
            // width > 1.0 -> m3
            if (dim.getWidth().compareTo(BigDecimal.ONE) > 0) {
                value = dim.getHeight().multiply(dim.getWidth()).multiply(dim.getLength());
                cubicMetersCount++;
            }
            // width == 1.0 && height == 1.0 -> mb
            else if (dim.getWidth().compareTo(BigDecimal.ONE) == 0 && dim.getHeight().compareTo(BigDecimal.ONE) == 0) {
                value = dim.getLength();
                runningMetersCount++;
            }
            // width <= 1.0 (reszta) -> m2
            else {
                value = dim.getHeight().multiply(dim.getLength());
                squareMetersCount++;
            }

            if (ScaffoldingOperationType.ASSEMBLY.equals(dim.getOperationType())) {
                calculatedDimension = calculatedDimension.add(value);
            } else {
                calculatedDimension = calculatedDimension.subtract(value);
            }
        }

        BigDecimal finalValue = calculatedDimension.add(Objects.requireNonNullElse(existingDimension, BigDecimal.ZERO));
        UnitDTO unit = determinateUnit(cubicMetersCount, squareMetersCount, dimensions.size(), runningMetersCount);

        return new DimensionResult(unit, finalValue);
    }


    private UnitDTO determinateUnit(int cubicMetersCount, int squareMetersCount, int dimensionSize, int runningMetersCount) {
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

        return unitService.findGlobalUnitBySymbol(unitSymbol);
    }


    private boolean hasDimensionChanged(ScaffoldingLogPosition existingPosition, List<ScaffoldingLogPositionDimensionBaseDTO> dimensions) {
        List<ScaffoldingLogPositionDimensionBaseDTO> currentDto =
                existingPosition.getDimensions().stream()
                        .map(scaffoldingLogPositionDimensionBaseMapper::toDto)
                        .toList();
        return areDimensionsNotEqual(currentDto, dimensions);
    }

    private boolean areDimensionsNotEqual(
            List<ScaffoldingLogPositionDimensionBaseDTO> existing,
            List<ScaffoldingLogPositionDimensionBaseDTO> updating) {

        if (existing.size() != updating.size()) {
            return true;
        }

        Set<DimensionKey> existingKeys = existing.stream()
                .map(this::createDimensionKey)
                .collect(Collectors.toSet());

        Set<DimensionKey> updatingKeys = updating.stream()
                .map(this::createDimensionKey)
                .collect(Collectors.toSet());

        return !existingKeys.equals(updatingKeys);
    }

    private boolean hasWorkingTimeChanged(ScaffoldingLogPosition existing, List<ScaffoldingLogPositionWorkingTimeBaseDTO> updating) {
        List<ScaffoldingLogPositionWorkingTimeBaseDTO> currentDto = existing.getWorkingTimes().stream()
                .map(scaffoldingLogPositionWorkingTimeBaseMapper::toDto)
                .toList();
        return areWorkingTimesNotEqual(currentDto, updating);
    }

    private boolean areWorkingTimesNotEqual(List<ScaffoldingLogPositionWorkingTimeBaseDTO> updating, List<ScaffoldingLogPositionWorkingTimeBaseDTO> existing) {
        if (existing.size() != updating.size()) {
            return true;
        }
        Set<WorkingTimeKey> existingKeys = existing.stream()
                .map(this::createWorkingTimeKey)
                .collect(Collectors.toSet());

        Set<WorkingTimeKey> updatingKeys = updating.stream()
                .map(this::createWorkingTimeKey)
                .collect(Collectors.toSet());
        return !existingKeys.equals(updatingKeys);
    }

    private String determineUnitSymbol(ScaffoldingLogPositionDimensionBaseDTO dimensionDTO) {
        if (dimensionDTO.getWidth().compareTo(BigDecimal.ONE) > 0) {
            return "m3"; // objętość
        } else if (dimensionDTO.getWidth().compareTo(BigDecimal.ONE) == 0
                && dimensionDTO.getHeight().compareTo(BigDecimal.ONE) == 0) {
            return "mb"; // długość
        } else {
            return "m2"; // powierzchnia
        }
    }

    private DimensionKey createDimensionKey(ScaffoldingLogPositionDimensionBaseDTO dto) {
        return new DimensionKey(
                dto.getId(),
                dto.getHeight(),
                dto.getWidth(),
                dto.getLength(),
                dto.getDimensionType(),
                dto.getDismantlingDate(),
                dto.getAssemblyDate(),
                dto.getOperationType(),
                dto.getUnit()
        );
    }

    private WorkingTimeKey createWorkingTimeKey(ScaffoldingLogPositionWorkingTimeBaseDTO dto) {
        return new WorkingTimeKey(
                dto.getId(),
                dto.getNumberOfWorkers(),
                dto.getNumberOfHours(),
                dto.getUnit(),
                dto.getOperationType()
        );
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

        ScaffoldingLogPosition positionToDelete = scaffoldingLogPositionDao.findById(aLong)
                .orElseThrow(() -> new ScaffoldingLogPositionException(
                        ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND,
                        "Scaffolding Log Position: " + aLong + " not found"));

        if (positionToDelete.getParentPosition() == null) {
            scaffoldingLogPositionDao.deleteById(positionToDelete.getId());
            return;
        }

        ScaffoldingLogPosition parent = positionToDelete.getParentPosition();
        List<ScaffoldingLogPosition> siblings = parent.getChildPositions();

        validateIsLatestModification(positionToDelete, siblings);

        recalculateParentBeforeDelete(parent, positionToDelete);

        parent.getChildPositions().remove(positionToDelete);
        scaffoldingLogPositionDao.deleteById(positionToDelete.getId());


        scaffoldingLogPositionDao.save(parent);

    }

    /**
     * Sprawdza, czy usuwana pozycja jest ostatnią w sekwencji modyfikacji.
     */
    private void validateIsLatestModification(ScaffoldingLogPosition toDelete, List<ScaffoldingLogPosition> siblings) {
        // Sortujemy rodzeństwo po dacie utworzenia (lub ID jeśli jest sekwencyjne) malejąco
        // Zakładam, że pole createdDate istnieje (z AuditableEntity)
        Optional<ScaffoldingLogPosition> latestSibling = siblings.stream()
                .max(Comparator.comparing(ScaffoldingLogPosition::getId)); // Lub getCreatedDate()

        if (latestSibling.isPresent()) {
            if (!latestSibling.get().getId().equals(toDelete.getId())) {
                throw new ScaffoldingLogPositionException(
                        ScaffoldingLogPositionErrorCode.CANNOT_DELETE_HISTORICAL_POSITION,
                        "Cannot delete historical modification. Only the latest sub-position can be deleted."
                );
            }
        }
    }

    /**
     * Przelicza stan rodzica tak, jakby usuwana podpozycja nigdy nie istniała.
     */
    private void recalculateParentBeforeDelete(ScaffoldingLogPosition parent, ScaffoldingLogPosition toDelete) {
        // 1. Wymiary
        // Musimy "odwrócić" wpływ usuwanej pozycji na rodzica.
        // Jeśli pozycja dodawała wymiar (ASSEMBLY), to teraz musimy go odjąć od rodzica.
        // Jeśli odejmowała (DISMANTLING), musimy go dodać.
        // ALE PROŚCIEJ: Przeliczyć rodzica od nowa biorąc pod uwagę wszystkie dzieci OPRÓCZ usuwanego.

        List<ScaffoldingLogPositionDimensionBaseDTO> allChildrenDimensions = parent.getChildPositions().stream()
                .filter(child -> !child.getId().equals(toDelete.getId())) // Pomiń usuwany
                .map(child -> child.getDimensions().stream()
                        .map(scaffoldingLogPositionDimensionBaseMapper::toDto)
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .toList();

        // Obliczamy nowy wymiar rodzica bazując na jego własnej bazie (ewentualnej) + reszcie dzieci
        // UWAGA: Tu zakładam, że rodzic jako ROOT ma swoje wymiary bazowe, a dzieci to delty.
        // Jeśli rodzic jest tylko sumą dzieci, to startujemy od ZERO.
        // Z analizy Twojego kodu wynika, że przekazujesz current parent dimension do calculateScaffoldingDimension.
        // Tutaj musimy zrobić inaczej: wziąć bazowy wymiar rodzica (bez dzieci) i nałożyć na niego resztę dzieci.

        // Jednak najbezpieczniej przy delcie jest po prostu odwrócić operację na liczbach:

        BigDecimal currentTotal = parent.getScaffoldingFullDimension();
        BigDecimal toDeleteDelta = calculateNetDimensionChange(toDelete.getDimensions());

        // Nowy total = obecny total - delta usuwanej pozycji
        BigDecimal newTotal = currentTotal.subtract(toDeleteDelta);
        parent.setScaffoldingFullDimension(newTotal);


        // 2. Czas pracy
        BigDecimal currentWorkingTime = parent.getFullWorkingTime();
        BigDecimal toDeleteTime = calculateFullWorkingTime(BigDecimal.ZERO,
                toDelete.getWorkingTimes().stream()
                        .map(scaffoldingLogPositionWorkingTimeBaseMapper::toDto)
                        .collect(Collectors.toList()));

        parent.setFullWorkingTime(currentWorkingTime.subtract(toDeleteTime));
    }

    // Pomocnicza metoda do obliczenia "wartości netto" danej pozycji (czy dodaje czy odejmuje)
    private BigDecimal calculateNetDimensionChange(List<ScaffoldingLogPositionDimension> dimensions) {
        BigDecimal total = BigDecimal.ZERO;
        for (ScaffoldingLogPositionDimension dim : dimensions) {
            BigDecimal value;
            if (dim.getWidth().compareTo(BigDecimal.ONE) > 0) {
                value = dim.getHeight().multiply(dim.getWidth()).multiply(dim.getLength());
            } else if (dim.getWidth().compareTo(BigDecimal.ONE) == 0 && dim.getHeight().compareTo(BigDecimal.ONE) == 0) {
                value = dim.getLength();
            } else {
                value = dim.getHeight().multiply(dim.getLength());
            }

            if (ScaffoldingOperationType.ASSEMBLY.equals(dim.getOperationType())) {
                total = total.add(value);
            } else {
                total = total.subtract(value);
            }
        }
        return total;
    }

    private record DimensionKey(
            Long id,
            BigDecimal height,
            BigDecimal width,
            BigDecimal length,
            DimensionType dimensionType,
            LocalDate dismantlingDate,
            LocalDate assemblyDate,
            ScaffoldingOperationType operationType,
            UnitBaseDTO unit
    ) {
    }

    private record WorkingTimeKey(
            Long id,
            Integer numberOfWorkers,
            BigDecimal numberOfHours,
            UnitBaseDTO unit,
            ScaffoldingOperationType operationType
    ) {
    }
}

