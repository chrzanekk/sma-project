package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.VehicleModel;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.repository.VehicleModelRepository;
import pl.com.chrzanowski.scma.service.VehicleModelService;
import pl.com.chrzanowski.scma.service.dto.VehicleModelDTO;
import pl.com.chrzanowski.scma.service.filter.vehiclemodel.VehicleModelFilter;
import pl.com.chrzanowski.scma.service.filter.vehiclemodel.VehicleModelSpecification;
import pl.com.chrzanowski.scma.service.mapper.VehicleModelMapper;
import pl.com.chrzanowski.scma.util.DateTimeUtil;
import pl.com.chrzanowski.scma.util.FieldValidator;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleModelServiceImpl implements VehicleModelService {
    private final Logger log = LoggerFactory.getLogger(VehicleModelServiceImpl.class);
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleModelMapper vehicleModelMapper;
    private final VehicleModelSpecification vehicleModelSpecification;

    public VehicleModelServiceImpl(VehicleModelRepository vehicleModelRepository,
                                   VehicleModelMapper vehicleModelMapper,
                                   VehicleModelSpecification vehicleModelSpecification) {
        this.vehicleModelRepository = vehicleModelRepository;
        this.vehicleModelMapper = vehicleModelMapper;
        this.vehicleModelSpecification = vehicleModelSpecification;
    }

    @Override
    public VehicleModelDTO save(VehicleModelDTO vehicleModelDTO) {
        log.debug("Save vehicle model: {}", vehicleModelDTO);
        validateVehicleModelDTO(vehicleModelDTO);
        VehicleModelDTO vehicleModelDTOtoSave =
                VehicleModelDTO.builder(vehicleModelDTO)
                        .createDate(DateTimeUtil.setDateTimeIfNotExists(vehicleModelDTO.getCreateDate())).build();
        VehicleModel vehicleModel = vehicleModelRepository.save(vehicleModelMapper.toEntity(vehicleModelDTO));
        return vehicleModelMapper.toDto(vehicleModel);
    }

    @Override
    public VehicleModelDTO update(VehicleModelDTO vehicleModelDTO) {
        log.debug("Update vehicle model: {}", vehicleModelDTO);
        validateVehicleModelDTO(vehicleModelDTO);
        FieldValidator.validateObject(vehicleModelDTO.getId(), "vehicleModelId");
        VehicleModelDTO vehicleModelDTOToUpdate =
                VehicleModelDTO.builder(vehicleModelDTO)
                        .modifyDate(DateTimeUtil.setDateTimeIfNotExists(vehicleModelDTO.getModifyDate()))
                        .build();
        VehicleModel vehicleModel = vehicleModelRepository.save(vehicleModelMapper.toEntity(vehicleModelDTOToUpdate));
        return vehicleModelMapper.toDto(vehicleModel);
    }

    @Override
    public List<VehicleModelDTO> findByFilter(VehicleModelFilter vehicleModelFilter) {
        log.debug("Find all vehicle models by filter: {}", vehicleModelFilter);
        Specification<VehicleModel> spec =
               VehicleModelSpecification.createSpecification(vehicleModelFilter);
        return vehicleModelMapper.toDto(vehicleModelRepository.findAll(spec));
    }

    @Override
    public Page<VehicleModelDTO> findByFilterAndPage(VehicleModelFilter vehicleModelFilter, Pageable pageable) {
        log.debug("Find all pageable vehicle models by filter: {}", vehicleModelFilter);
        Specification<VehicleModel> spec =
                VehicleModelSpecification.createSpecification(vehicleModelFilter);
        return vehicleModelRepository.findAll(spec, pageable).map(vehicleModelMapper::toDto);
    }

    @Override
    public VehicleModelDTO findById(Long id) {
        log.debug("Find vehicle model by id: {}", id);
        FieldValidator.validateObject(id, "vehicleModelId");
        Optional<VehicleModel> vehicleBrand = vehicleModelRepository.findById(id);
        return vehicleModelMapper.toDto(vehicleBrand.orElseThrow(() -> new ObjectNotFoundException("Vehicle model " +
                "not found")));
    }

    @Override
    public List<VehicleModelDTO> findAll() {
        log.debug("Find all vehicle models.");
        List<VehicleModel> vehicleModelList = vehicleModelRepository.findAll();
        return vehicleModelMapper.toDto(vehicleModelList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete vehicle model by id: {}", id);
        FieldValidator.validateObject(id, "vehicleModelId");
        vehicleModelRepository.deleteVehicleModelById(id);
    }

    private void validateVehicleModelDTO(VehicleModelDTO vehicleModelDTO) {
        FieldValidator.validateObject(vehicleModelDTO, "vehicleModelDTO");
        FieldValidator.validateString(vehicleModelDTO.getName(), "VehicleModelName");
        FieldValidator.validateString(vehicleModelDTO.getVehicleBrandName(), "VehicleBrandName");
        FieldValidator.validateObject(vehicleModelDTO.getVehicleBrandId(), "VehicleBrandId");
    }
}
