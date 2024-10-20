package pl.com.chrzanowski.scma.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.FuelType;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.repository.FuelTypeRepository;
import pl.com.chrzanowski.scma.service.FuelTypeService;
import pl.com.chrzanowski.scma.service.dto.FuelTypeDTO;
import pl.com.chrzanowski.scma.service.filter.fueltype.FuelTypeFilter;
import pl.com.chrzanowski.scma.service.filter.fueltype.FuelTypeSpecification;
import pl.com.chrzanowski.scma.service.mapper.FuelTypeMapper;
import pl.com.chrzanowski.scma.util.DateTimeUtil;
import pl.com.chrzanowski.scma.util.FieldValidator;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FuelTypeServiceImpl implements FuelTypeService {

    private final Logger log = LoggerFactory.getLogger(FuelTypeServiceImpl.class);
    private final FuelTypeRepository fuelTypeRepository;
    private final FuelTypeMapper fuelTypeMapper;

    private final FuelTypeSpecification fuelTypeSpecification;

    public FuelTypeServiceImpl(FuelTypeRepository fuelTypeRepository,
                               FuelTypeMapper fuelTypeMapper,
                               FuelTypeSpecification fuelTypeSpecification) {
        this.fuelTypeRepository = fuelTypeRepository;
        this.fuelTypeMapper = fuelTypeMapper;
        this.fuelTypeSpecification = fuelTypeSpecification;
    }

    @Override
    public FuelTypeDTO save(FuelTypeDTO fuelTypeDTO) {
        log.debug("Save fuel type: {}", fuelTypeDTO);
        validateFuelTypeDTO(fuelTypeDTO);
        FuelTypeDTO fuelTypeToSave = FuelTypeDTO.builder(fuelTypeDTO)
                .createDate(DateTimeUtil.setDateTimeIfNotExists(fuelTypeDTO.getCreateDate())).build();
        FuelType fuelType = fuelTypeRepository.save(fuelTypeMapper.toEntity(fuelTypeToSave));
        return fuelTypeMapper.toDto(fuelType);
    }

    @Override
    public FuelTypeDTO update(FuelTypeDTO fuelTypeDTO) {
        log.debug("Update fuel type: {}", fuelTypeDTO);
        validateFuelTypeDTO(fuelTypeDTO);
        FieldValidator.validateObject(fuelTypeDTO.getId(), "FuelTypeId");
        FuelTypeDTO fuelTypeInDB = findById(fuelTypeDTO.getId());
        FuelTypeDTO fuelTypeToUpdate = FuelTypeDTO.builder(fuelTypeDTO)
                .createDate(DateTimeUtil.setDateTimeIfNotExists(fuelTypeInDB.getCreateDate()))
                .modifyDate(DateTimeUtil.setDateTimeIfNotExists(fuelTypeDTO.getModifyDate())).build();
        FuelType fuelType = fuelTypeRepository.save(fuelTypeMapper.toEntity(fuelTypeToUpdate));
        return fuelTypeMapper.toDto(fuelType);
    }

    @Override
    public FuelTypeDTO findById(Long id) {
        log.debug("Find fuel type by id: {}", id);
        FieldValidator.validateObject(id, "FuelTypeId");
        Optional<FuelType> fuelTypeOptional = fuelTypeRepository.findById(id);
        return fuelTypeMapper.toDto(fuelTypeOptional.orElseThrow(() -> new ObjectNotFoundException("Fuel type not " + "found")));
    }

    @Override
    public List<FuelTypeDTO> findAll() {
        log.debug("Find all fuel types.");
        List<FuelType> fuelTypeList = fuelTypeRepository.findAll();
        return fuelTypeMapper.toDto(fuelTypeList);
    }

    @Override
    public List<FuelTypeDTO> findByFilter(FuelTypeFilter fuelTypeFilter) {
        log.debug("Find all fuel types by filter: {}.", fuelTypeFilter);
        Specification<FuelType> spec = FuelTypeSpecification.createSpecification(fuelTypeFilter);
        return fuelTypeMapper.toDto(fuelTypeRepository.findAll(spec));
    }

    @Override
    public Page<FuelTypeDTO> findByFilterAndPage(FuelTypeFilter fuelTypeFilter, Pageable pageable) {
        log.debug("Find all pageable fuel types by filter: {}.", fuelTypeFilter);
        Specification<FuelType> spec = FuelTypeSpecification.createSpecification(fuelTypeFilter);
        return fuelTypeRepository.findAll(spec, pageable).map(fuelTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete fuel typ of id: {}", id);
        FieldValidator.validateObject(id, "fuelTypeId");
        fuelTypeRepository.deleteFuelTypeById(id);
    }

    private void validateFuelTypeDTO(FuelTypeDTO fuelTypeDTO) {
        FieldValidator.validateObject(fuelTypeDTO, "fuelTypeDTO");
        FieldValidator.validateString(fuelTypeDTO.getName(), "name");
    }
}
