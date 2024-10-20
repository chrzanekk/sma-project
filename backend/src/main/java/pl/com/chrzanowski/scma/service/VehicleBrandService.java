package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.VehicleBrandDTO;
import pl.com.chrzanowski.scma.service.filter.vehiclebrand.VehicleBrandFilter;

import java.util.List;

public interface VehicleBrandService {

    VehicleBrandDTO save(VehicleBrandDTO vehicleBrandDTO);

    VehicleBrandDTO update(VehicleBrandDTO vehicleBrandDTO);

    List<VehicleBrandDTO> findByFilter(VehicleBrandFilter vehicleBrandFilter);
    Page<VehicleBrandDTO> findByFilterAndPage(VehicleBrandFilter vehicleBrandFilter, Pageable pageable);

    VehicleBrandDTO findById(Long id);

    List<VehicleBrandDTO> findAll();

    void delete(Long id);
}
