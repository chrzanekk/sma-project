package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.VehicleBrand;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.repository.VehicleBrandRepository;
import pl.com.chrzanowski.scma.service.VehicleBrandService;
import pl.com.chrzanowski.scma.service.dto.VehicleBrandDTO;
import pl.com.chrzanowski.scma.service.filter.vehiclebrand.VehicleBrandFilter;
import pl.com.chrzanowski.scma.service.filter.vehiclebrand.VehicleBrandSpecification;
import pl.com.chrzanowski.scma.service.mapper.VehicleBrandMapper;
import pl.com.chrzanowski.scma.util.DateTimeUtil;
import pl.com.chrzanowski.scma.util.FieldValidator;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final Logger log = LoggerFactory.getLogger(VehicleBrandServiceImpl.class);
    private final VehicleBrandRepository vehicleBrandRepository;
    private final VehicleBrandMapper vehicleBrandMapper;
    private final VehicleBrandSpecification vehicleBrandSpecification;

    public VehicleBrandServiceImpl(VehicleBrandRepository vehicleBrandRepository,
                                   VehicleBrandMapper vehicleBrandMapper,
                                   VehicleBrandSpecification vehicleBrandSpecification) {
        this.vehicleBrandRepository = vehicleBrandRepository;
        this.vehicleBrandMapper = vehicleBrandMapper;
        this.vehicleBrandSpecification = vehicleBrandSpecification;
    }

    @Override
    public VehicleBrandDTO save(VehicleBrandDTO vehicleBrandDTO) {
        log.debug("Save vehicle brand: {}", vehicleBrandDTO);
        validateVehicleBrandDTO(vehicleBrandDTO);
        VehicleBrandDTO vehicleBrandDTOToSave =
                VehicleBrandDTO.builder(vehicleBrandDTO)
                        .createDate(DateTimeUtil.setDateTimeIfNotExists(vehicleBrandDTO.getCreateDate())).build();
        VehicleBrand vehicleBrand = vehicleBrandRepository.save(vehicleBrandMapper.toEntity(vehicleBrandDTOToSave));
        return vehicleBrandMapper.toDto(vehicleBrand);
    }

    @Override
    public VehicleBrandDTO update(VehicleBrandDTO vehicleBrandDTO) {
        log.debug("Update vehicle brand: {}", vehicleBrandDTO);
        validateVehicleBrandDTO(vehicleBrandDTO);
        FieldValidator.validateObject(vehicleBrandDTO.getId(), "vehicleBrandId");
        VehicleBrandDTO vehicleBrandDTOToUpdate =
                VehicleBrandDTO.builder(vehicleBrandDTO)
                        .modifyDate(DateTimeUtil.setDateTimeIfNotExists(vehicleBrandDTO.getModifyDate())).build();
        VehicleBrand vehicleBrand = vehicleBrandRepository.save(vehicleBrandMapper.toEntity(vehicleBrandDTOToUpdate));
        return vehicleBrandMapper.toDto(vehicleBrand);
    }

    @Override
    public List<VehicleBrandDTO> findByFilter(VehicleBrandFilter vehicleBrandFilter) {
        log.debug("Find all vehicle brands by filter: {}", vehicleBrandFilter);
        Specification<VehicleBrand> spec =
                VehicleBrandSpecification.createSpecification(vehicleBrandFilter);
        return vehicleBrandMapper.toDto(vehicleBrandRepository.findAll(spec));
    }

    @Override
    public Page<VehicleBrandDTO> findByFilterAndPage(VehicleBrandFilter vehicleBrandFilter,
                                                     Pageable pageable) {
        log.debug("Find all pageable vehicle brands by filter: {}", vehicleBrandFilter);
        Specification<VehicleBrand> spec =
                VehicleBrandSpecification.createSpecification(vehicleBrandFilter);
        return vehicleBrandRepository.findAll(spec, pageable).map(vehicleBrandMapper::toDto);
    }

    @Override
    public VehicleBrandDTO findById(Long id) {
        log.debug("Find vehicle brand by id: {}", id);
        FieldValidator.validateObject(id, "vehicleBrandId");
        Optional<VehicleBrand> vehicleBrand = vehicleBrandRepository.findById(id);
        return vehicleBrandMapper.toDto(vehicleBrand.orElseThrow(() -> new ObjectNotFoundException("Vehicle brand " +
                "not found")));
    }

    @Override
    public List<VehicleBrandDTO> findAll() {
        log.debug("Find all vehicle brands.");
        List<VehicleBrand> vehicleBrandList = vehicleBrandRepository.findAll();
        return vehicleBrandMapper.toDto(vehicleBrandList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete vehicle bran by id: {}", id);
        FieldValidator.validateObject(id, "vehicleBrandId");
        vehicleBrandRepository.deleteVehicleBrandById(id);
    }

    private void validateVehicleBrandDTO(VehicleBrandDTO vehicleBrandDTO) {
        FieldValidator.validateObject(vehicleBrandDTO, "vehicleBranDTO");
        FieldValidator.validateString(vehicleBrandDTO.getName(), "vehicleBranName");
    }
}
