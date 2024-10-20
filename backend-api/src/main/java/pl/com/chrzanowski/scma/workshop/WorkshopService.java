package pl.com.chrzanowski.scma.workshop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkshopService {
    WorkshopDTO save(WorkshopDTO workshopDTO);

    WorkshopDTO update(WorkshopDTO workshopDTO);

    List<WorkshopDTO> findByFilter(WorkshopFilter workshopFilter);

    Page<WorkshopDTO> findByFilterAndPage(WorkshopFilter workshopFilter, Pageable pageable);

    WorkshopDTO findById(Long id);

    List<WorkshopDTO> findAll();

    void delete(Long id);
}
