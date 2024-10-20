package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.ServiceActionTypeDTO;
import pl.com.chrzanowski.scma.service.filter.serviceactiontype.ServiceActionTypeFilter;

import java.util.List;

public interface ServiceActionTypeService {

    ServiceActionTypeDTO save(ServiceActionTypeDTO serviceActionTypeDTO);

    ServiceActionTypeDTO update(ServiceActionTypeDTO serviceActionTypeDTO);

    List<ServiceActionTypeDTO> findByFilter(ServiceActionTypeFilter serviceActionTypeFilter);
    Page<ServiceActionTypeDTO> findByFilterAndPage(ServiceActionTypeFilter serviceActionTypeFilter, Pageable pageable);

    ServiceActionTypeDTO findById(Long id);

    List<ServiceActionTypeDTO> findAll();

    void delete(Long id);
}
