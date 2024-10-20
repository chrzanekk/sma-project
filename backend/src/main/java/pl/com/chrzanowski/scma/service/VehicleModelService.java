package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.VehicleModelDTO;
import pl.com.chrzanowski.scma.service.filter.vehiclemodel.VehicleModelFilter;

import java.util.List;

public interface VehicleModelService {

    VehicleModelDTO save(VehicleModelDTO vehicleBrandDTO);

    VehicleModelDTO update(VehicleModelDTO vehicleBrandDTO);

    List<VehicleModelDTO> findByFilter(VehicleModelFilter vehicleModelFilter);

    Page<VehicleModelDTO> findByFilterAndPage(VehicleModelFilter vehicleModelFilter, Pageable pageable);

    VehicleModelDTO findById(Long id);

    List<VehicleModelDTO> findAll();

    void delete(Long id);
}
