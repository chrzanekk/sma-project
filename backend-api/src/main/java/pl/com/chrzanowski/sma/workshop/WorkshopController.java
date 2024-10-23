package pl.com.chrzanowski.sma.workshop;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.util.controller.PaginationUtil;

import java.util.List;

@RestController
@RequestMapping(path = "/api/workshops")
public class WorkshopController {

    private final Logger log = LoggerFactory.getLogger(WorkshopController.class);
    private static final String ENTITY_NAME = "workshop";
    private final WorkshopService workshopService;

    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkshopDTO>> getAllWorkshops() {
        log.debug("REST request to get all workshops");
        List<WorkshopDTO> workshopDTOS = workshopService.findAll();
        return ResponseEntity.ok().body(workshopDTOS);
    }

    @GetMapping("/")
    public ResponseEntity<List<WorkshopDTO>> getAllWorkshopsByFilter(WorkshopFilter workshopFilter) {
        log.debug("REST request to get all workshops by filter: {}", workshopFilter);
        List<WorkshopDTO> workshopDTOS = workshopService.findByFilter(workshopFilter);
        return ResponseEntity.ok().body(workshopDTOS);
    }

    @GetMapping("/page")
    public ResponseEntity<List<WorkshopDTO>> getAllWorkshopsByFilterAndPage(WorkshopFilter workshopFilter,
                                                                            Pageable pageable) {
        log.debug("REST request to get all workshops by filter: {}", workshopFilter);
        Page<WorkshopDTO> page = workshopService.findByFilterAndPage(workshopFilter,
                pageable);
        HttpHeaders headers =
                PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(),
                        page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<WorkshopDTO> getWorkshopById(@Valid @PathVariable Long id) {
        log.debug("REST request to get workshop by id: {}", id);
        WorkshopDTO workshopDTO = workshopService.findById(id);
        return ResponseEntity.ok().body(workshopDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<WorkshopDTO> addWorkshop(@RequestBody WorkshopDTO workshopDTO) {
        log.debug("REST request to add new workshop: {}", workshopDTO);
        WorkshopDTO newWorkshopDTO = workshopService.save(workshopDTO);
        return ResponseEntity.ok().body(newWorkshopDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<WorkshopDTO> updateWorkshop(@RequestBody WorkshopDTO workshopDTO) {
        log.debug("RST request to update workshop: {}", workshopDTO);
        WorkshopDTO updatedWorkshopDTO = workshopService.update(workshopDTO);
        return ResponseEntity.ok().body(updatedWorkshopDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkshop(@PathVariable Long id) {
        log.debug("REST request to delete workshop of id: {}", id);
        workshopService.delete(id);
        return ResponseEntity.ok().build();
    }
}
