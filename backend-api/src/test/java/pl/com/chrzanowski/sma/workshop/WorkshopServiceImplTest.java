package pl.com.chrzanowski.sma.workshop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkshopServiceImplTest {

    @Mock
    private WorkshopRepository workshopRepository;

    @Mock
    private WorkshopMapper workshopMapper;

    @Mock
    private WorkshopSpecification workshopSpecification;

    @InjectMocks
    private WorkshopServiceImpl workshopService;

    private WorkshopDTO workshopDTO;
    private Workshop workshop;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        workshopDTO = WorkshopDTO.builder()
                .id(1L)
                .name("Workshop 1")
                .createdDatetime(Instant.now())
                .build();

        workshop = new Workshop();
        workshop.setId(1L);
        workshop.setName("Workshop 1");
    }


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveWorkshopSuccess() {
        when(workshopMapper.toEntity(any(WorkshopDTO.class))).thenReturn(workshop);
        when(workshopRepository.save(any(Workshop.class))).thenReturn(workshop);
        when(workshopMapper.toDto(any(Workshop.class))).thenReturn(workshopDTO);

        WorkshopDTO result = workshopService.save(workshopDTO);

        assertNotNull(result);
        assertEquals("Workshop 1", result.getName());

        verify(workshopMapper, times(1)).toEntity(any(WorkshopDTO.class));
        verify(workshopRepository, times(1)).save(any(Workshop.class));
        verify(workshopMapper, times(1)).toDto(any(Workshop.class));
    }

    @Test
    void testUpdateWorkshopSuccess() {
        when(workshopMapper.toEntity(any(WorkshopDTO.class))).thenReturn(workshop);
        when(workshopRepository.save(any(Workshop.class))).thenReturn(workshop);
        when(workshopMapper.toDto(any(Workshop.class))).thenReturn(workshopDTO);

        WorkshopDTO result = workshopService.update(workshopDTO);

        assertNotNull(result);
        assertEquals("Workshop 1", result.getName());

        verify(workshopMapper, times(1)).toEntity(any(WorkshopDTO.class));
        verify(workshopRepository, times(1)).save(any(Workshop.class));
        verify(workshopMapper, times(1)).toDto(any(Workshop.class));
    }

    @Test
    void testFindByFilterSuccess() {
        WorkshopFilter filter = new WorkshopFilter();

        when(workshopRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(workshop));
        when(workshopMapper.toDto(anyList())).thenReturn(Collections.singletonList(workshopDTO));

        List<WorkshopDTO> result = workshopService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(workshopRepository, times(1)).findAll(any(Specification.class));
        verify(workshopMapper, times(1)).toDto(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        WorkshopFilter filter = new WorkshopFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Workshop> workshopPage = new PageImpl<>(Collections.singletonList(workshop));
        when(workshopRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(workshopPage);
        when(workshopMapper.toDto(any(Workshop.class))).thenReturn(workshopDTO);

        Page<WorkshopDTO> result = workshopService.findByFilterAndPage(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(workshopRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(workshopMapper, times(1)).toDto(any(Workshop.class));
    }

    @Test
    void testFindByIdSuccess() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));
        when(workshopMapper.toDto(any(Workshop.class))).thenReturn(workshopDTO);

        WorkshopDTO result = workshopService.findById(1L);

        assertNotNull(result);
        assertEquals("Workshop 1", result.getName());

        verify(workshopRepository, times(1)).findById(1L);
        verify(workshopMapper, times(1)).toDto(any(Workshop.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(workshopRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> workshopService.findById(1L));

        verify(workshopRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllSuccess() {
        when(workshopRepository.findAll()).thenReturn(Collections.singletonList(workshop));
        when(workshopMapper.toDto(anyList())).thenReturn(Collections.singletonList(workshopDTO));

        List<WorkshopDTO> result = workshopService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(workshopRepository, times(1)).findAll();
        verify(workshopMapper, times(1)).toDto(anyList());
    }

    @Test
    void testDeleteSuccess() {
        workshopService.delete(1L);

        verify(workshopRepository, times(1)).deleteWorkshopById(1L);
    }
}
