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
import pl.com.chrzanowski.scma.service.VehicleTypeService;
import pl.com.chrzanowski.scma.service.dto.VehicleTypeDTO;
import pl.com.chrzanowski.scma.service.filter.vehicletype.VehicleTypeFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicleTypes")
public class VehicleTypeController {

    private final Logger log = LoggerFactory.getLogger(VehicleTypeController.class);
    private static final String ENTITY_NAME = "vehicleType";
    private final VehicleTypeService vehicleTypeService;

    public VehicleTypeController(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleTypeDTO>> getAllVehicleTypes() {
        log.debug("REST request to get all vehicleTypes");
        List<VehicleTypeDTO> vehicleTypeDTOList = vehicleTypeService.findAll();
        return ResponseEntity.ok().body(vehicleTypeDTOList);
    }

    @GetMapping("/")
    public ResponseEntity<List<VehicleTypeDTO>> getAllVehicleTypesByFilter(VehicleTypeFilter vehicleTypeFilter) {
        log.debug("REST request to get all vehicleTypes by filter: {}", vehicleTypeFilter);
        List<VehicleTypeDTO> vehicleTypeDTOList = vehicleTypeService.findByFilter(vehicleTypeFilter);
        return ResponseEntity.ok().body(vehicleTypeDTOList);
    }

    @GetMapping("/page")
    public ResponseEntity<List<VehicleTypeDTO>> getAllVehicleTypesByFilterAndPage(VehicleTypeFilter vehicleTypeFilter,
                                                                                  Pageable pageable) {
        log.debug("REST request to get all vehicleTypes by filter: {}", vehicleTypeFilter);
        Page<VehicleTypeDTO> page = vehicleTypeService.findByFilterAndPage(vehicleTypeFilter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), page);
        return ResponseEntity.ok()
                .headers(headers)
                .body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<VehicleTypeDTO> getVehicleTypeById(@PathVariable Long id) {
        log.debug("REST request to get vehicleType by id: {}", id);
        VehicleTypeDTO vehicleTypeDTO = vehicleTypeService.findById(id);
        return ResponseEntity.ok().body(vehicleTypeDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleTypeDTO> addVehicleType(@RequestBody VehicleTypeDTO vehicleTypeDTO) {
        log.debug("REST request to add new vehicleType: {}", vehicleTypeDTO);
        VehicleTypeDTO newVehicleTypeDTO = vehicleTypeService.save(vehicleTypeDTO);
        return ResponseEntity.ok().body(newVehicleTypeDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<VehicleTypeDTO> updateVehicleType(@RequestBody VehicleTypeDTO vehicleTypeDTO) {
        log.debug("RST request to update vehicleType: {}", vehicleTypeDTO);
        VehicleTypeDTO updatedVehicleTypeDTO = vehicleTypeService.update(vehicleTypeDTO);
        return ResponseEntity.ok().body(updatedVehicleTypeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicleType(@PathVariable Long id) {
        log.debug("REST request to delete vehicleType of id: {}", id);
        vehicleTypeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
