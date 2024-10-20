package pl.com.chrzanowski.scma.workshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.util.DateTimeUtil;

import java.util.List;
import java.util.Optional;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    private final Logger log = LoggerFactory.getLogger(WorkshopServiceImpl.class);
    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;
    private final WorkshopSpecification workshopSpecification;

    public WorkshopServiceImpl(WorkshopRepository workshopRepository,
                               WorkshopMapper workshopMapper,
                               WorkshopSpecification workshopSpecification) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
        this.workshopSpecification = workshopSpecification;
    }

    @Override
    public WorkshopDTO save(WorkshopDTO workshopDTO) {
        log.debug("Save workshop: {}", workshopDTO);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder(workshopDTO)
                .createDate(DateTimeUtil.setDateTimeIfNotExists(workshopDTO.getCreateDate())).build();
        Workshop workshop = workshopRepository.save(workshopMapper.toEntity(workshopDTOtoSave));
        return workshopMapper.toDto(workshop);
    }

    @Override
    public WorkshopDTO update(WorkshopDTO workshopDTO) {
        log.debug("Update workshop: {}", workshopDTO);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder(workshopDTO)
                .modifyDate(DateTimeUtil.setDateTimeIfNotExists(workshopDTO.getModifyDate())).build();
        Workshop workshop = workshopRepository.save(workshopMapper.toEntity(workshopDTOtoUpdate));
        return workshopMapper.toDto(workshop);
    }

    @Override
    public List<WorkshopDTO> findByFilter(WorkshopFilter workshopFilter) {
        log.debug("Find all workshops by filter: {}", workshopFilter);
        Specification<Workshop> spec = WorkshopSpecification.createSpecification(workshopFilter);
        return workshopMapper.toDto(workshopRepository.findAll(spec));
    }

    @Override
    public Page<WorkshopDTO> findByFilterAndPage(WorkshopFilter workshopFilter, Pageable pageable) {
        log.debug("Find all workshops by filter and page: {}", workshopFilter);
        Specification<Workshop> spec = WorkshopSpecification.createSpecification(workshopFilter);
        return workshopRepository.findAll(spec,pageable).map(workshopMapper::toDto);
    }

    @Override
    public WorkshopDTO findById(Long id) {
        log.debug("Find workshop by id: {}", id);
        Optional<Workshop> workshopOptional = workshopRepository.findById(id);
        return workshopMapper.toDto(workshopOptional.orElseThrow(() -> new ObjectNotFoundException("Workshop not " +
                "found")));
    }

    @Override
    public List<WorkshopDTO> findAll() {
        log.debug("Find all workshops.");
        List<Workshop> workshopList = workshopRepository.findAll();
        return workshopMapper.toDto(workshopList);
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete workshop by id: {}", id);
        workshopRepository.deleteWorkshopById(id);
    }
}
