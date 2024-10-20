package pl.com.chrzanowski.scma.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.scma.controller.util.PaginationUtil;
import pl.com.chrzanowski.scma.service.SummaryValueServiceActionService;
import pl.com.chrzanowski.scma.service.dto.ServiceActionDTO;
import pl.com.chrzanowski.scma.service.dto.SummaryValueServiceActionDTO;
import pl.com.chrzanowski.scma.service.filter.serviceaction.ServiceActionFilter;
import pl.com.chrzanowski.scma.service.impl.ServiceActionServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/api/serviceActions")
public class ServiceActionController {

    private final Logger log = LoggerFactory.getLogger(ServiceActionController.class);
    private static final String ENTITY_NAME = "serviceAction";
    private final ServiceActionServiceImpl serviceActionService;
    private final SummaryValueServiceActionService summaryValueServiceActionService;

    public ServiceActionController(ServiceActionServiceImpl serviceActionService,
                                   SummaryValueServiceActionService summaryValueServiceActionService) {
        this.serviceActionService = serviceActionService;
        this.summaryValueServiceActionService = summaryValueServiceActionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceActionDTO>> getAlLServiceActions() {
        log.debug("REST request to get all service actions");
        List<ServiceActionDTO> result = serviceActionService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiceActionDTO>> getAllServiceActionsByFilter(ServiceActionFilter filter) {
        log.debug("REST request to get all service actions by filter: {} ", filter);
        List<ServiceActionDTO> serviceActionDTOS = serviceActionService.findByFilter(filter);
        return ResponseEntity.ok().body(serviceActionDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ServiceActionDTO>> getAllServiceActionsByFilterAndPage(ServiceActionFilter filter,
                                                                                      Pageable pageable) {
        log.debug("REST request to get all service actions by filter: {} ", filter);
        Page<ServiceActionDTO> page = serviceActionService.findByFilterAndPage(filter, pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ServiceActionDTO> getServiceActionById(@Valid @PathVariable Long id) {
        log.debug("REST request to get service action by id: {}", id);
        ServiceActionDTO serviceActionDTO = serviceActionService.findById(id);
        return ResponseEntity.ok().body(serviceActionDTO);
    }

    @GetMapping("/getSummaryValues")
    public ResponseEntity<SummaryValueServiceActionDTO> getSummaryValuesOfServiceActions() {
        log.debug("REST request to get summary values of all service actions");
        SummaryValueServiceActionDTO result = summaryValueServiceActionService.getSummaryValues();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/add")
    public ResponseEntity<ServiceActionDTO> addServiceAction(@RequestBody ServiceActionDTO serviceActionDTO) {
        log.debug("REST request to add new service action: {}", serviceActionDTO);
        ServiceActionDTO newServiceActionDTO = serviceActionService.save(serviceActionDTO);
        return ResponseEntity.ok().body(newServiceActionDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceActionDTO> updateServiceAction(@RequestBody ServiceActionDTO serviceActionDTO) {
        log.debug("REST request to update service action: {}", serviceActionDTO);
        ServiceActionDTO updatedServiceActionDTO = serviceActionService.update(serviceActionDTO);
        return ResponseEntity.ok().body(updatedServiceActionDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteServiceAction(@PathVariable Long id) {
        log.debug("REST request to delete service action of id: {}", id);
        serviceActionService.delete(id);
        return ResponseEntity.ok().build();
    }

}
