package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.VehicleDTO;
import pl.com.chrzanowski.scma.service.filter.vehicle.VehicleFilter;

import java.util.List;

public interface VehicleService {

    VehicleDTO save(VehicleDTO vehicleDTO);

    VehicleDTO update(VehicleDTO vehicleDTO);

    List<VehicleDTO> findByFilter(VehicleFilter filter);

    Page<VehicleDTO> findByFilterAndPage(VehicleFilter filter, Pageable pageable);

    VehicleDTO findById(Long id);

    List<VehicleDTO> findAll();

    void delete(Long id);
}
