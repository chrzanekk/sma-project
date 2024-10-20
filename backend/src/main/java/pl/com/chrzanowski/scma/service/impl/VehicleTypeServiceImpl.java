package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.VehicleType;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.repository.VehicleTypeRepository;
import pl.com.chrzanowski.scma.service.VehicleTypeService;
import pl.com.chrzanowski.scma.service.dto.VehicleTypeDTO;
import pl.com.chrzanowski.scma.service.filter.vehicletype.VehicleTypeFilter;
import pl.com.chrzanowski.scma.service.filter.vehicletype.VehicleTypeSpecification;
import pl.com.chrzanowski.scma.service.mapper.VehicleTypeMapper;
import pl.com.chrzanowski.scma.util.DateTimeUtil;
import pl.com.chrzanowski.scma.util.FieldValidator;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final Logger log = LoggerFactory.getLogger(VehicleTypeServiceImpl.class);
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleTypeMapper vehicleTypeMapper;
    private final VehicleTypeSpecification vehicleTypeSpecification;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository,
                                  VehicleTypeMapper vehicleTypeMapper,
                                  VehicleTypeSpecification vehicleTypeSpecification) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleTypeMapper = vehicleTypeMapper;
        this.vehicleTypeSpecification = vehicleTypeSpecification;
    }

    @Override
    public VehicleTypeDTO save(VehicleTypeDTO vehicleTypeDTO) {
        log.debug("Save vehicle type: {}", vehicleTypeDTO);
        validateVehicleTypeDTO(vehicleTypeDTO);
        VehicleTypeDTO vehicleTypeDTOToSave =
                VehicleTypeDTO.builder(vehicleTypeDTO)
                        .createDate(DateTimeUtil.setDateTimeIfNotExists(vehicleTypeDTO.getCreateDate())).build();
        VehicleType vehicleType = vehicleTypeRepository.save(vehicleTypeMapper.toEntity(vehicleTypeDTOToSave));
        return vehicleTypeMapper.toDto(vehicleType);
    }

    @Override
    public VehicleTypeDTO update(VehicleTypeDTO vehicleTypeDTO) {
        log.debug("Update vehicle type: {}", vehicleTypeDTO);
        validateVehicleTypeDTO(vehicleTypeDTO);
        FieldValidator.validateObject(vehicleTypeDTO.getId(), "vehicleTypeId");
        VehicleTypeDTO vehicleTypeDTOToUpdate =
                VehicleTypeDTO.builder(vehicleTypeDTO)
                        .modifyDate(DateTimeUtil.setDateTimeIfNotExists(vehicleTypeDTO.getModifyDate())).build();
        VehicleType vehicleType = vehicleTypeRepository.save(vehicleTypeMapper.toEntity(vehicleTypeDTOToUpdate));
        return vehicleTypeMapper.toDto(vehicleType);
    }

    @Override
    public List<VehicleTypeDTO> findByFilter(VehicleTypeFilter vehicleTypeFilter) {
        log.debug("Find all types by filter: {}", vehicleTypeFilter);
        Specification<VehicleType> spec =
                VehicleTypeSpecification.createSpecification(vehicleTypeFilter);
        return vehicleTypeMapper.toDto(vehicleTypeRepository.findAll(spec));
    }

    @Override
    public Page<VehicleTypeDTO> findByFilterAndPage(VehicleTypeFilter vehicleTypeFilter,
                                                    Pageable pageable) {
        log.debug("Find all pageable types by filter: {}", vehicleTypeFilter);
        Specification<VehicleType> spec =
                VehicleTypeSpecification.createSpecification(vehicleTypeFilter);
        return vehicleTypeRepository.findAll(spec, pageable).map(vehicleTypeMapper::toDto);
    }

    @Override
    public VehicleTypeDTO findById(Long id) {
        log.debug("Find vehicle type by id: {}", id);
        FieldValidator.validateObject(id, "vehicleTypeId");
        Optional<VehicleType> vehicleType = vehicleTypeRepository.findById(id);
        return vehicleTypeMapper.toDto(vehicleType.orElseThrow(() -> new ObjectNotFoundException(" Vehicle type " +
                "not found")));
    }

    @Override
    public List<VehicleTypeDTO> findAll() {
        log.debug("Find all vehicle types.");
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        return vehicleTypeMapper.toDto(vehicleTypeList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete vehicle type of id: {}", id);
        FieldValidator.validateObject(id, "vehicleTypeId");
        vehicleTypeRepository.deleteVehicleTypeById(id);
    }

    private void validateVehicleTypeDTO(VehicleTypeDTO vehicleTypeDTO) {
        FieldValidator.validateObject(vehicleTypeDTO, "vehicleTypeDTO");
        FieldValidator.validateString(vehicleTypeDTO.getName(), "vehicleTypeName");
    }
}
