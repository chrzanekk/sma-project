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
import pl.com.chrzanowski.scma.service.TireService;
import pl.com.chrzanowski.scma.service.dto.TireDTO;
import pl.com.chrzanowski.scma.service.filter.tire.TireFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tires")
public class TireController {
    private final Logger log = LoggerFactory.getLogger(TireController.class);
    private static final String ENTITY_NAME = "tire";
    private final TireService tireService;

    public TireController(TireService tireService) {
        this.tireService = tireService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TireDTO>> getAllVehicleTires() {
        log.debug("REST request to get all vehicle tires");
        List<TireDTO> tireDTOList = tireService.findAll();
        return ResponseEntity.ok().body(tireDTOList);
    }

    @GetMapping("/")
    public ResponseEntity<List<TireDTO>> getAllVehicleTiresByFilter(TireFilter tireFilter) {
        log.debug("REST request to get all vehicle tires by filter: {}", tireFilter);
        List<TireDTO> tireDTOList = tireService.findByFilter(tireFilter);
        return ResponseEntity.ok().body(tireDTOList);
    }

    @GetMapping("/page")
    public ResponseEntity<List<TireDTO>> getAllVehicleTiresByFilterAndPage(TireFilter tireFilter,
                                                                           Pageable pageable) {
        log.debug("REST request to get all vehicle tires by filter: {}", tireFilter);
        Page<TireDTO> page = tireService.findByFilterAndPage(tireFilter,
                pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<TireDTO> getVehicleTireById(@Valid @PathVariable Long id) {
        log.debug("REST request to get vehicle tire by id: {}", id);
        TireDTO tireDTO = tireService.findById(id);
        return ResponseEntity.ok().body(tireDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<TireDTO> addVehicleTire(@Valid @RequestBody TireDTO tireDTO) {
        log.debug("REST request to add new vehicle tire: {}", tireDTO);
        TireDTO newTireDTO = tireService.save(tireDTO);
        return ResponseEntity.ok().body(newTireDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<TireDTO> updateVehicleTire(@Valid @RequestBody TireDTO tireDTO) {
        log.debug("RST request to update vehicle: {}", tireDTO);
        TireDTO updatedTireDTO = tireService.update(tireDTO);
        return ResponseEntity.ok().body(updatedTireDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicleTire(@PathVariable Long id) {
        log.debug("REST request to delete vehicle tire by id: {}", id);
        tireService.delete(id);
        return ResponseEntity.ok().build();
    }
}
