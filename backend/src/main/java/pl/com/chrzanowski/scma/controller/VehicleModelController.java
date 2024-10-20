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
import pl.com.chrzanowski.scma.service.VehicleModelService;
import pl.com.chrzanowski.scma.service.dto.VehicleModelDTO;
import pl.com.chrzanowski.scma.service.filter.vehiclemodel.VehicleModelFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicleModels")
public class VehicleModelController {
    private final Logger log = LoggerFactory.getLogger(VehicleModelController.class);
    private static final String ENTITY_NAME = "vehicleModel";
    private final VehicleModelService vehicleModelService;

    public VehicleModelController(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModels() {
        log.debug("REST request to get all vehicleModels");
        List<VehicleModelDTO> vehicleModelDTOS = vehicleModelService.findAll();
        return ResponseEntity.ok().body(vehicleModelDTOS);
    }

    @GetMapping("/")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModelsByFilter(VehicleModelFilter vehicleModelFilter) {
        log.debug("REST request to get all vehicleModels by filter: {}", vehicleModelFilter);
        List<VehicleModelDTO> vehicleModelDTOList = vehicleModelService.findByFilter(vehicleModelFilter);
        return ResponseEntity.ok().body(vehicleModelDTOList);
    }

    @GetMapping("/page")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModelsByFilterAndPage(VehicleModelFilter vehicleModelFilter,
                                                                                    Pageable pageable) {
        log.debug("REST request to get all vehicleModels by filter: {}", vehicleModelFilter);
        Page<VehicleModelDTO> page = vehicleModelService.findByFilterAndPage(vehicleModelFilter,
                pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<VehicleModelDTO> getVehicleModelById(@PathVariable Long id) {
        log.debug("REST request to get vehicleModel by id: {}", id);
        VehicleModelDTO vehicleBrandDTO = vehicleModelService.findById(id);
        return ResponseEntity.ok().body(vehicleBrandDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleModelDTO> addVehicleModel(@RequestBody VehicleModelDTO vehicleModelDTO) {
        log.debug("REST request to add new vehicleModel: {}", vehicleModelDTO);
        VehicleModelDTO newVehicleModelDTO = vehicleModelService.save(vehicleModelDTO);
        return ResponseEntity.ok().body(newVehicleModelDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<VehicleModelDTO> updateVehicleModel(@RequestBody VehicleModelDTO vehicleModelDTO) {
        log.debug("RST request to update vehicleModel: {}", vehicleModelDTO);
        VehicleModelDTO updatedVehicleModelDTO = vehicleModelService.update(vehicleModelDTO);
        return ResponseEntity.ok().body(updatedVehicleModelDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicleModel(@PathVariable Long id) {
        log.debug("REST request to delete vehicleModel of id: {}", id);
        vehicleModelService.delete(id);
        return ResponseEntity.ok().build();
    }
}
