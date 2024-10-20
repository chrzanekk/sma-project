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
import pl.com.chrzanowski.scma.service.VehicleBrandService;
import pl.com.chrzanowski.scma.service.dto.VehicleBrandDTO;
import pl.com.chrzanowski.scma.service.filter.vehiclebrand.VehicleBrandFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vehicleBrands")
public class VehicleBrandController {

    private final Logger log = LoggerFactory.getLogger(VehicleBrandController.class);
    private static final String ENTITY_NAME = "vehicleBrand";
    private final VehicleBrandService vehicleBrandService;

    public VehicleBrandController(VehicleBrandService vehicleBrandService) {
        this.vehicleBrandService = vehicleBrandService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleBrandDTO>> getAllVehicleBrands() {
        log.debug("REST request to get all vehicleBrands");
        List<VehicleBrandDTO> vehicleBrandDTOList = vehicleBrandService.findAll();
        return ResponseEntity.ok().body(vehicleBrandDTOList);
    }

    @GetMapping("/")
    public ResponseEntity<List<VehicleBrandDTO>> getAllVehicleBrandsByFilter(VehicleBrandFilter vehicleBrandFilter) {
        log.debug("REST request to get all vehicleBrands by filter: {}", vehicleBrandFilter);
        List<VehicleBrandDTO> vehicleBrandDTOList = vehicleBrandService.findByFilter(vehicleBrandFilter);
        return ResponseEntity.ok().body(vehicleBrandDTOList);
    }

    @GetMapping("/page")
    public ResponseEntity<List<VehicleBrandDTO>> getAllVehicleBrandsByFilterAndPage(VehicleBrandFilter vehicleBrandFilter,
                                                                                    Pageable pageable) {
        log.debug("REST request to get all vehicleBrands by filter: {}", vehicleBrandFilter);
        Page<VehicleBrandDTO> page = vehicleBrandService.findByFilterAndPage(vehicleBrandFilter,
                pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<VehicleBrandDTO> getVehicleBrandById(@PathVariable Long id) {
        log.debug("REST request to get vehicleBrand by id: {}", id);
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandService.findById(id);
        return ResponseEntity.ok().body(vehicleBrandDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleBrandDTO> addVehicleBrand(@RequestBody VehicleBrandDTO vehicleBrandDTO) {
        log.debug("REST request to add new vehicleBrand: {}", vehicleBrandDTO);
        VehicleBrandDTO newVehicleBrandDTO = vehicleBrandService.save(vehicleBrandDTO);
        return ResponseEntity.ok().body(newVehicleBrandDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<VehicleBrandDTO> updateVehicleBrand(@RequestBody VehicleBrandDTO vehicleBrandDTO) {
        log.debug("RST request to update vehicleBrand: {}", vehicleBrandDTO);
        VehicleBrandDTO updatedVehicleBrandDTO = vehicleBrandService.update(vehicleBrandDTO);
        return ResponseEntity.ok().body(updatedVehicleBrandDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicleBrand(@PathVariable Long id) {
        log.debug("REST request to delete vehicleBrand of id: {}", id);
        vehicleBrandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
