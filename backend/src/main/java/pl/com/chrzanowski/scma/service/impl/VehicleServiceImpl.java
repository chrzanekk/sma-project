package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.Vehicle;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.repository.VehicleRepository;
import pl.com.chrzanowski.scma.service.VehicleService;
import pl.com.chrzanowski.scma.service.dto.VehicleDTO;
import pl.com.chrzanowski.scma.service.filter.vehicle.VehicleFilter;
import pl.com.chrzanowski.scma.service.filter.vehicle.VehicleSpecification;
import pl.com.chrzanowski.scma.service.mapper.VehicleMapper;
import pl.com.chrzanowski.scma.util.DateTimeUtil;
import pl.com.chrzanowski.scma.util.FieldValidator;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleSpecification vehicleSpecification;

    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                              VehicleMapper vehicleMapper,
                              VehicleSpecification vehicleSpecification) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.vehicleSpecification = vehicleSpecification;
    }

    @Override
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        log.debug("Save vehicle: {}", vehicleDTO);
        validateVehicleDTO(vehicleDTO);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder(vehicleDTO)
                .createDate(DateTimeUtil.setDateTimeIfNotExists(vehicleDTO.getCreateDate())).build();
        Vehicle vehicle = vehicleRepository.save(vehicleMapper.toEntity(vehicleDTO));
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        log.debug("Update vehicle: {}", vehicleDTO);
        validateVehicleDTO(vehicleDTO);
        FieldValidator.validateObject(vehicleDTO.getId(), "vehicleId");
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder(vehicleDTO)
                .modifyDate(DateTimeUtil.setDateTimeIfNotExists(vehicleDTO.getModifyDate())).build();
        Vehicle vehicle = vehicleRepository.save(vehicleMapper.toEntity(vehicleDTO));
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public List<VehicleDTO> findByFilter(VehicleFilter filter) {
        log.debug("Find all vehicles by filter: {}", filter);
        Specification<Vehicle> spec = VehicleSpecification.createSpecification(filter);
        return vehicleMapper.toDto(vehicleRepository.findAll(spec));
    }

    @Override
    public Page<VehicleDTO> findByFilterAndPage(VehicleFilter filter, Pageable pageable) {
        log.debug("Find all vehicles by filter and page: {}", filter);
        Specification<Vehicle> spec = VehicleSpecification.createSpecification(filter);
        return vehicleRepository.findAll(spec, pageable).map(vehicleMapper::toDto);
    }

    @Override
    public VehicleDTO findById(Long id) {
        log.debug("Find vehicle by id: {}", id);
        FieldValidator.validateObject(id, "vehicleId");
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        return vehicleMapper.toDto(vehicleOptional.orElseThrow(() -> new ObjectNotFoundException("Vehicle not found")));
    }

    @Override
    public List<VehicleDTO> findAll() {
        log.debug("Find all vehicles.");
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleMapper.toDto(vehicleList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete vehicle by id: {}", id);
        FieldValidator.validateObject(id, "vehicleId");
        vehicleRepository.deleteVehicleById(id);

    }

    private void validateVehicleDTO(VehicleDTO vehicleDTO) {
        FieldValidator.validateObject(vehicleDTO, "vehicleDTO");
        FieldValidator.validateObject(vehicleDTO.getBrandId(), "vehicleBrandId");
        FieldValidator.validateObject(vehicleDTO.getModelId(), "vehicleModelId");
        FieldValidator.validateObject(vehicleDTO.getFuelTypeId(), "fuelTypeId");
        FieldValidator.validateObject(vehicleDTO.getVehicleTypeId(), "vehicleTypeId");
        FieldValidator.validateString(vehicleDTO.getBrandName(), "vehicleBrandName");
        FieldValidator.validateString(vehicleDTO.getModelName(), "vehicleModelName");
        FieldValidator.validateString(vehicleDTO.getFuelTypeName(), "fuelTypeName");
        FieldValidator.validateString(vehicleDTO.getVehicleTypeName(), "vehicleTypeName");
        FieldValidator.validateString(vehicleDTO.getRegistrationNumber(), "registrationNumber");
        FieldValidator.validateString(vehicleDTO.getVin(), "vin");
        FieldValidator.validateObject(vehicleDTO.getProductionYear(), "productionYear");
        FieldValidator.validateObject(vehicleDTO.getFirstRegistrationDate(), "firstRegistrationYear");
        FieldValidator.validateObject(vehicleDTO.getFreePlacesForTechInspection(), "freePlacesForTechInspection");
        FieldValidator.validateObject(vehicleDTO.getLength(), "length");
        FieldValidator.validateObject(vehicleDTO.getWidth(), "width");
        FieldValidator.validateObject(vehicleDTO.getHeight(), "height");
    }
}
