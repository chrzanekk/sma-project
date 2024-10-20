package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.VehicleTypeDTO;
import pl.com.chrzanowski.scma.service.filter.vehicletype.VehicleTypeFilter;

import java.util.List;

public interface VehicleTypeService {

    VehicleTypeDTO save(VehicleTypeDTO vehicleTypeDTO);

    VehicleTypeDTO update(VehicleTypeDTO vehicleTypeDTO);

    List<VehicleTypeDTO> findByFilter(VehicleTypeFilter vehicleTypeFilter);
    Page<VehicleTypeDTO> findByFilterAndPage(VehicleTypeFilter vehicleTypeFilter, Pageable pageable);

    VehicleTypeDTO findById(Long id);

    List<VehicleTypeDTO> findAll();

    void delete(Long id);
}
