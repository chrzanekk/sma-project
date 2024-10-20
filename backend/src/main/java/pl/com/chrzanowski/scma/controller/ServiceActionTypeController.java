package pl.com.chrzanowski.scma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.scma.controller.util.PaginationUtil;
import pl.com.chrzanowski.scma.service.ServiceActionTypeService;
import pl.com.chrzanowski.scma.service.dto.ServiceActionTypeDTO;
import pl.com.chrzanowski.scma.service.filter.serviceactiontype.ServiceActionTypeFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/serviceActionTypes")
public class ServiceActionTypeController {

    private final Logger log = LoggerFactory.getLogger(ServiceActionTypeController.class);
    private static final String ENTITY_NAME = "serviceActionType";
    private final ServiceActionTypeService serviceActionTypeService;

    public ServiceActionTypeController(ServiceActionTypeService serviceActionTypeService) {
        this.serviceActionTypeService = serviceActionTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceActionTypeDTO>> getAllServiceTypes() {
        log.debug("REST request to get all serviceActionTypes");
        List<ServiceActionTypeDTO> serviceActionTypeDTOList = serviceActionTypeService.findAll();
        return ResponseEntity.ok().body(serviceActionTypeDTOList);
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiceActionTypeDTO>> getAllServiceTypesByFilter(ServiceActionTypeFilter serviceActionTypeFilter) {
        log.debug("REST request to get all serviceActionTypes by filter: {}", serviceActionTypeFilter);
        List<ServiceActionTypeDTO> serviceActionTypeDTOList = serviceActionTypeService.findByFilter(serviceActionTypeFilter);
        return ResponseEntity.ok()
                .body(serviceActionTypeDTOList);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ServiceActionTypeDTO>> getAllServiceTypesByFilterAndPage(ServiceActionTypeFilter serviceActionTypeFilter,
                                                                                        Pageable pageable) {
        log.debug("REST request to pageable get all serviceActionTypes by filter: {}", serviceActionTypeFilter);
        Page<ServiceActionTypeDTO> page =
                serviceActionTypeService.findByFilterAndPage(serviceActionTypeFilter, pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return ResponseEntity.ok()
                .headers(headers)
                .body(page.getContent());
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<ServiceActionTypeDTO> getServiceActionTypeById(@PathVariable Long id) {
        log.debug("REST request to get serviceActionType by id: {}", id);
        ServiceActionTypeDTO serviceActionTypeDTO = serviceActionTypeService.findById(id);
        return ResponseEntity.ok().body(serviceActionTypeDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ServiceActionTypeDTO> addServiceActionType(@RequestBody ServiceActionTypeDTO serviceActionTypeDTO) {
        log.debug("REST request to add new serviceActionType: {}", serviceActionTypeDTO);
        ServiceActionTypeDTO newServiceActionTypeDTO = serviceActionTypeService.save(serviceActionTypeDTO);
        return ResponseEntity.ok().body(newServiceActionTypeDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceActionTypeDTO> updateServiceActionType(@RequestBody ServiceActionTypeDTO serviceActionTypeDTO) {
        log.debug("REST request to update new serviceActionType: {}", serviceActionTypeDTO);
        ServiceActionTypeDTO updatedServiceActionTypeDTO = serviceActionTypeService.update(serviceActionTypeDTO);
        return ResponseEntity.ok().body(updatedServiceActionTypeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteServiceType(@PathVariable Long id) {
        log.debug("REST request to delete serviceType by id: {}", id);
        serviceActionTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

}
