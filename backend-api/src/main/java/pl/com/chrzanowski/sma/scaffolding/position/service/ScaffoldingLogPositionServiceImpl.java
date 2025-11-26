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
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.service.ContactService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.dto.ScaffoldingLogPositionDimensionDTO;
import pl.com.chrzanowski.sma.scaffolding.dimension.mapper.ScaffoldingLogPositionDimensionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.ScaffoldingLogPositionDimensionService;
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDao;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.position.mapper.ScaffoldingLogPositionDTOMapper;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.validator.ScaffoldingNumberValidationService;
import pl.com.chrzanowski.sma.scaffolding.workingtime.mapper.ScaffoldingLogPositionWorkingTimeBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.ScaffoldingLogPositionWorkingTimeService;
import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;
import pl.com.chrzanowski.sma.unit.dto.UnitDTO;
import pl.com.chrzanowski.sma.unit.mapper.UnitBaseMapper;
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
    private final ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper;
    private final ScaffoldingNumberValidationService scaffoldingNumberValidationService;
    private final UnitService unitService;
    private final UnitBaseMapper unitBaseMapper;
    private final ContractorService contractorService;
    private final ContractorBaseMapper contractorBaseMapper;
    private final ContactService contactService;
    private final ContactBaseMapper contactBaseMapper;
    private final ScaffoldingLogPositionDimensionService scaffoldingLogPositionDimensionService;
    private final ScaffoldingLogPositionDimensionBaseMapper scaffoldingLogPositionDimensionBaseMapper;
    private final ScaffoldingLogPositionWorkingTimeService scaffoldingLogPositionWorkingTimeService;
    private final ScaffoldingLogPositionWorkingTimeBaseMapper scaffoldingLogPositionWorkingTimeBaseMapper;
    private final CompanyBaseMapper companyBaseMapper;


    public ScaffoldingLogPositionServiceImpl(ScaffoldingLogPositionDao scaffoldingLogPositionDao, ScaffoldingLogPositionDTOMapper scaffoldingLogPositionDTOMapper, ScaffoldingLogPositionBaseMapper scaffoldingLogPositionBaseMapper, ScaffoldingNumberValidationService scaffoldingNumberValidationService, UnitService unitService, UnitBaseMapper unitBaseMapper, ContractorService contractorService, ContractorBaseMapper contractorBaseMapper, ContactService contactService, ContactBaseMapper contactBaseMapper, ScaffoldingLogPositionDimensionService scaffoldingLogPositionDimensionService, ScaffoldingLogPositionDimensionBaseMapper scaffoldingLogPositionDimensionBaseMapper, ScaffoldingLogPositionWorkingTimeService scaffoldingLogPositionWorkingTimeService, ScaffoldingLogPositionWorkingTimeBaseMapper scaffoldingLogPositionWorkingTimeBaseMapper, CompanyBaseMapper companyBaseMapper) {
        this.scaffoldingLogPositionDao = scaffoldingLogPositionDao;
        this.scaffoldingLogPositionDTOMapper = scaffoldingLogPositionDTOMapper;
        this.scaffoldingLogPositionBaseMapper = scaffoldingLogPositionBaseMapper;
        this.scaffoldingNumberValidationService = scaffoldingNumberValidationService;
        this.unitService = unitService;
        this.unitBaseMapper = unitBaseMapper;
        this.contractorService = contractorService;
        this.contractorBaseMapper = contractorBaseMapper;
        this.contactService = contactService;
        this.contactBaseMapper = contactBaseMapper;
        this.scaffoldingLogPositionDimensionService = scaffoldingLogPositionDimensionService;
        this.scaffoldingLogPositionDimensionBaseMapper = scaffoldingLogPositionDimensionBaseMapper;
        this.scaffoldingLogPositionWorkingTimeService = scaffoldingLogPositionWorkingTimeService;
        this.scaffoldingLogPositionWorkingTimeBaseMapper = scaffoldingLogPositionWorkingTimeBaseMapper;
        this.companyBaseMapper = companyBaseMapper;
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
            createDto = recalculateParentDimensions(createDto);
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
        log.debug("Request to update ScaffoldingLogPosition : {}", updateDto.getId());

        Long updatedId = updateDto.getId();
        ScaffoldingLogPosition existingPosition = scaffoldingLogPositionDao.findById(updateDto.getId())
                .orElseThrow(() -> new ScaffoldingLogPositionException(ScaffoldingLogPositionErrorCode.SCAFFOLDING_LOG_POSITION_NOT_FOUND, "Scaffolding Log Position with id " + updatedId + " not found"));

        scaffoldingLogPositionDTOMapper.updateFromDto(updateDto, existingPosition);
        // check contractor and scaffolding user with their contacts
        updateContractor(updateDto, existingPosition);
        updateContractorContact(updateDto, existingPosition);
        updateScaffoldingUser(updateDto, existingPosition);
        updateScaffoldingUserContact(updateDto, existingPosition);

        //check dimension change and recalculate if needed
        List<ScaffoldingLogPositionDimensionBaseDTO> existingDimensionDTOList = scaffoldingLogPositionDimensionService.findByScaffoldingLogPositionId(existingPosition.getId());
        List<ScaffoldingLogPositionDimensionBaseDTO> updatingDimensionDTOList = updateDto.getDimensions();

        boolean dimensionsChanged = !areDimensionsEqual(existingDimensionDTOList, updatingDimensionDTOList);

        if (dimensionsChanged) {
            updateDimensions(existingPosition, existingDimensionDTOList, updatingDimensionDTOList);

            // Recalculate dimensions for current position
            Map<UnitBaseDTO, BigDecimal> calculatedDimension =
                    calculateScaffoldingDimension(BigDecimal.ZERO, updatingDimensionDTOList, existingPosition.getCompany().getId());
            Map.Entry<UnitBaseDTO, BigDecimal> entry = calculatedDimension.entrySet().iterator().next();

            existingPosition.setScaffoldingFullDimension(entry.getValue());
            existingPosition.setScaffoldingFullDimensionUnit(unitBaseMapper.toEntity(entry.getKey()));

            // Recalculate parent position if exists
            if (existingPosition.getParentPosition() != null) {
                updateDto = recalculateParentDimensions(updateDto);
                existingPosition.setParentPosition(scaffoldingLogPositionBaseMapper.toEntity(updateDto.getParentPosition()));
            }
        }

        //todo check changed working time - next step

        scaffoldingNumberValidationService.validateScaffoldingNumber(updateDto.getScaffoldingNumber());


        //update only main fields of entity

        ScaffoldingLogPosition updatedPosition = scaffoldingLogPositionDao.save(existingPosition);
        return findById(updatedPosition.getId());
    }

    private void updateScaffoldingUserContact(ScaffoldingLogPositionDTO updateDto, ScaffoldingLogPosition existingPosition) {
        if (!existingPosition.getScaffoldingUserContact().getId().equals(updateDto.getScaffoldingUserContact().getId())) {
            ContactBaseDTO contactBaseDTO = contactService.findById(updateDto.getScaffoldingUserContact().getId());
            Contact contactToUpdate = contactBaseMapper.toEntity(contactBaseDTO);
            existingPosition.setScaffoldingUserContact(contactToUpdate);
        }
    }

    private void updateScaffoldingUser(ScaffoldingLogPositionDTO updateDto, ScaffoldingLogPosition existingPosition) {
        if (!existingPosition.getScaffoldingUser().getId().equals(updateDto.getScaffoldingUser().getId())) {
            ContractorBaseDTO scaffoldingUser = contractorService.findById(updateDto.getScaffoldingUser().getId());
            if (!scaffoldingUser.getCustomer()) {
                throw new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_CUSTOMER, "Contractor is not a customer");
            }
            if (!scaffoldingUser.getScaffoldingUser()) {
                throw new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_SCAFFOLDING_USER, "Contractor is not a scaffolding user");
            }
            Contractor scaffoldingUserToUpdate = contractorBaseMapper.toEntity(scaffoldingUser);
            existingPosition.setScaffoldingUser(scaffoldingUserToUpdate);
        }
    }

    private void updateContractorContact(ScaffoldingLogPositionDTO updateDto, ScaffoldingLogPosition existingPosition) {
        if (!existingPosition.getContractorContact().getId().equals(updateDto.getContractorContact().getId())) {
            ContactBaseDTO contactBaseDTO = contactService.findById(updateDto.getContractorContact().getId());
            Contact contactToUpdate = contactBaseMapper.toEntity(contactBaseDTO);
            existingPosition.setContractorContact(contactToUpdate);
        }
    }

    private void updateContractor(ScaffoldingLogPositionDTO updateDto, ScaffoldingLogPosition existingPosition) {
        if (!existingPosition.getContractor().getId().equals(updateDto.getContractor().getId())) {
            ContractorBaseDTO contractorBaseDTO = contractorService.findById(updateDto.getContractor().getId());
            if (!contractorBaseDTO.getCustomer()) {
                throw new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_CUSTOMER, "Contractor is not a customer");
            }
            Contractor contractor = contractorBaseMapper.toEntity(contractorBaseDTO);
            existingPosition.setContractor(contractor);
        }
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

        UnitDTO unitDTO = unitService.findGlobalUnitBySymbol(unitSymbol);

        Map<UnitBaseDTO, BigDecimal> result = new HashMap<>();
        result.put(unitDTO, calculatedDimension);
        return result;
    }


    private boolean areDimensionsEqual(
            List<ScaffoldingLogPositionDimensionBaseDTO> existing,
            List<ScaffoldingLogPositionDimensionBaseDTO> updating) {

        if (existing.size() != updating.size()) {
            return false;
        }

        // Create sets for comparison (ignoring order)
        Set<DimensionKey> existingKeys = existing.stream()
                .map(this::createDimensionKey)
                .collect(Collectors.toSet());

        Set<DimensionKey> updatingKeys = updating.stream()
                .map(this::createDimensionKey)
                .collect(Collectors.toSet());

        return existingKeys.equals(updatingKeys);
    }

    private void updateDimensions(
            ScaffoldingLogPosition existingPosition,
            List<ScaffoldingLogPositionDimensionBaseDTO> existingDTOList,
            List<ScaffoldingLogPositionDimensionBaseDTO> updatingDTOList) {

        // Create maps for easier lookup by ID
        Map<Long, ScaffoldingLogPositionDimensionBaseDTO> existingMap = existingDTOList.stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(ScaffoldingLogPositionDimensionBaseDTO::getId, Function.identity()));

        Map<Long, ScaffoldingLogPositionDimensionBaseDTO> updatingMap = updatingDTOList.stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(ScaffoldingLogPositionDimensionBaseDTO::getId, Function.identity()));

        // Find dimensions to delete (in existing but not in updating)
        Set<Long> existingIds = existingMap.keySet();
        Set<Long> updatingIds = updatingMap.keySet();

        List<Long> toDelete = existingIds.stream()
                .filter(id -> !updatingIds.contains(id))
                .toList();

        List<ScaffoldingLogPositionDimension> dimensionsToRemove = existingPosition.getDimensions().stream()
                .filter(d -> toDelete.contains(d.getId()))
                .toList();

        // Usuń z kolekcji używając helper method
        dimensionsToRemove.forEach(existingPosition::removeDimension);

        // Delete removed dimensions
        toDelete.forEach(id -> {
            log.debug("Deleting dimension with id: {}", id);
            scaffoldingLogPositionDimensionService.delete(id);
        });

        // Process updating dimensions
        for (ScaffoldingLogPositionDimensionBaseDTO updatingDTO : updatingDTOList) {
            if (updatingDTO.getId() != null && existingMap.containsKey(updatingDTO.getId())) {
                // Update existing dimension if fields changed
                ScaffoldingLogPositionDimensionBaseDTO existingDTO = existingMap.get(updatingDTO.getId());
                if (!areDimensionFieldsEqual(existingDTO, updatingDTO)) {
                    log.debug("Updating dimension with id: {}", updatingDTO.getId());
                    // Convert to full DTO with company and position
                    ScaffoldingLogPositionDimensionDTO toUpdate = convertToFullDTO(updatingDTO, existingPosition);
                    scaffoldingLogPositionDimensionService.update(toUpdate);
                }
            } else {
                // Add new dimension (ID is null or not in existing map)
                log.debug("Adding new dimension");
                ScaffoldingLogPositionDimensionDTO newDimension = convertToFullDTO(updatingDTO, existingPosition);
                scaffoldingLogPositionDimensionService.save(newDimension);
            }
        }
    }

    private boolean areDimensionFieldsEqual(
            ScaffoldingLogPositionDimensionBaseDTO dto1,
            ScaffoldingLogPositionDimensionBaseDTO dto2) {

        return Objects.equals(dto1.getId(), dto2.getId()) &&
                Objects.equals(dto1.getHeight(), dto2.getHeight()) &&
                Objects.equals(dto1.getWidth(), dto2.getWidth()) &&
                Objects.equals(dto1.getLength(), dto2.getLength()) &&
                Objects.equals(dto1.getDimensionType(), dto2.getDimensionType()) &&
                Objects.equals(dto1.getDismantlingDate(), dto2.getDismantlingDate()) &&
                Objects.equals(dto1.getAssemblyDate(), dto2.getAssemblyDate()) &&
                Objects.equals(dto1.getOperationType(), dto2.getOperationType());
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
                dto.getOperationType()
        );
    }

    private ScaffoldingLogPositionDimensionDTO convertToFullDTO(
            ScaffoldingLogPositionDimensionBaseDTO baseDTO,
            ScaffoldingLogPosition position) {

        return ScaffoldingLogPositionDimensionDTO.builder()
                .id(baseDTO.getId())
                .height(baseDTO.getHeight())
                .width(baseDTO.getWidth())
                .length(baseDTO.getLength())
                .dimensionType(baseDTO.getDimensionType())
                .dismantlingDate(baseDTO.getDismantlingDate())
                .assemblyDate(baseDTO.getAssemblyDate())
                .operationType(baseDTO.getOperationType())
                .company(companyBaseMapper.toDto(position.getCompany()))
                .scaffoldingPosition(scaffoldingLogPositionBaseMapper.toDto(position))
                .unit(baseDTO.getUnit())
                .build();
    }

    private ScaffoldingLogPositionDTO recalculateParentDimensions(ScaffoldingLogPositionDTO dto) {
        log.debug("Recalculating parent position dimensions for id: {}", dto.getParentPosition().getId());

        ScaffoldingLogPositionDTO parentPosition = findById(dto.getParentPosition().getId());
        BigDecimal parentPositionDimension = dto.getParentPosition().getScaffoldingFullDimension();

        Map<UnitBaseDTO, BigDecimal> parentDimensionResult = calculateScaffoldingDimension(parentPositionDimension, dto.getDimensions(), dto.getCompany().getId());

        Map.Entry<UnitBaseDTO, BigDecimal> parentEntry = parentDimensionResult.entrySet().iterator().next();

        parentPosition = parentPosition.toBuilder()
                .scaffoldingFullDimension(parentEntry.getValue())
                .scaffoldingFullDimensionUnit(parentEntry.getKey())
                .build();
        ScaffoldingLogPosition parentPositionEntity = scaffoldingLogPositionBaseMapper.toEntity(parentPosition);
        parentPositionEntity = scaffoldingLogPositionDao.save(parentPositionEntity);
        return dto.toBuilder().parentPosition(scaffoldingLogPositionBaseMapper.toDto(parentPositionEntity)).build();
    }

    private record DimensionKey(
            Long id,
            BigDecimal height,
            BigDecimal width,
            BigDecimal length,
            DimensionType dimensionType,
            LocalDate dismantlingDate,
            LocalDate assemblyDate,
            ScaffoldingOperationType operationType
    ) {
    }
}
