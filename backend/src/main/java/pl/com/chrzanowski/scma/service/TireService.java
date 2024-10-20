package pl.com.chrzanowski.scma.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.scma.service.dto.TireDTO;
import pl.com.chrzanowski.scma.service.filter.tire.TireFilter;

import java.util.List;

public interface TireService {


    TireDTO save(TireDTO tireDTO);

    TireDTO update(TireDTO tireDTO);

    List<TireDTO> findByFilter(TireFilter filter);

    Page<TireDTO> findByFilterAndPage(TireFilter filter, Pageable pageable);

    TireDTO findById(Long id);

    List<TireDTO> findAll();

    void delete(Long id);
}
