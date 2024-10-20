package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.FuelTypeDTO;
import pl.com.chrzanowski.scma.service.filter.fueltype.FuelTypeFilter;

import java.util.List;

public interface FuelTypeService {
    FuelTypeDTO save(FuelTypeDTO fuelTypeDTO);

    FuelTypeDTO update(FuelTypeDTO fuelTypeDTO);

    List<FuelTypeDTO> findByFilter(FuelTypeFilter fuelTypeFilter);
    
    Page<FuelTypeDTO> findByFilterAndPage(FuelTypeFilter fuelTypeFilter, Pageable pageable);

    FuelTypeDTO findById(Long id);

    List<FuelTypeDTO> findAll();

    void delete(Long id);
}
