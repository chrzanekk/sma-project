package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.ServiceActionDTO;
import pl.com.chrzanowski.scma.service.filter.serviceaction.ServiceActionFilter;

import java.util.List;

public interface ServiceActionService {

    ServiceActionDTO save(ServiceActionDTO serviceActionDTO);
    ServiceActionDTO update(ServiceActionDTO serviceActionDTO);
    List<ServiceActionDTO> findByFilter(ServiceActionFilter serviceActionFilter);
    Page<ServiceActionDTO> findByFilterAndPage(ServiceActionFilter serviceActionFilter, Pageable pageable);
    ServiceActionDTO findById(Long id);
    List<ServiceActionDTO> findAll();
    void delete(Long id);
}
