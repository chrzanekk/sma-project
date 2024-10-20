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
import pl.com.chrzanowski.scma.service.VehicleService;
import pl.com.chrzanowski.scma.service.dto.VehicleDTO;
import pl.com.chrzanowski.scma.service.filter.vehicle.VehicleFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicles")
public class VehicleController {

    private final Logger log = LoggerFactory.getLogger(VehicleController.class);
    private static final String ENTITY_NAME = "vehicle";
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        log.debug("REST request to get all vehicles");
        List<VehicleDTO> vehicleDTOList = vehicleService.findAll();
        return ResponseEntity.ok().body(vehicleDTOList);
    }

    @GetMapping("/")
    public ResponseEntity<List<VehicleDTO>> getAllVehiclesByFilter(VehicleFilter vehicleFilter) {
        log.debug("REST request to get all vehicles by filter: {}", vehicleFilter);
        List<VehicleDTO> vehicleDTOList = vehicleService.findByFilter(vehicleFilter);
        return ResponseEntity.ok().body(vehicleDTOList);
    }

    @GetMapping("/page")
    public ResponseEntity<List<VehicleDTO>> getAllVehiclesByFilterAndPage(VehicleFilter vehicleFilter,
                                                                          Pageable pageable) {
        log.debug("REST request to get all vehicles by filter and page: {}", vehicleFilter);
        Page<VehicleDTO> page = vehicleService.findByFilterAndPage(vehicleFilter,
                pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@Valid @PathVariable Long id) {
        log.debug("REST request to get vehicle by id: {}", id);
        VehicleDTO vehicleDTO = vehicleService.findById(id);
        return ResponseEntity.ok().body(vehicleDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleDTO> addVehicle(@RequestBody VehicleDTO vehicleDTO) {
        log.debug("REST request to add new vehicle: {}", vehicleDTO);
        VehicleDTO newVehicleDTO = vehicleService.save(vehicleDTO);
        return ResponseEntity.ok().body(newVehicleDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        log.debug("REST request to update vehicle: {}", vehicleDTO);
        VehicleDTO updatedVehicleDTO = vehicleService.update(vehicleDTO);
        return ResponseEntity.ok().body(updatedVehicleDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        log.debug("REST request to delete vehicle by id: {}", id);
        vehicleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
