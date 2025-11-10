package pl.com.chrzanowski.sma.unitTests.scaffolding.worktype.service;

import com.querydsl.core.BooleanBuilder;
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
import pl.com.chrzanowski.sma.scaffolding.worktype.dao.WorkTypeDao;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.dto.WorkTypeDTO;
import pl.com.chrzanowski.sma.scaffolding.worktype.mapper.WorkTypeAuditMapper;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.WorkTypeQueryServiceImpl;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkTypeQueryServiceImplTest {

    @Mock
    private WorkTypeDao workTypeDao;

    @Mock
    private WorkTypeAuditMapper workTypeAuditMapper;

    @InjectMocks
    private WorkTypeQueryServiceImpl workTypeQueryService;

    private WorkTypeDTO workTypeDTO;
    private WorkTypeAuditableDTO workTypeAuditableDTO;
    private WorkType workType;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        workTypeDTO = WorkTypeDTO.builder()
                .id(1L)
                .name("Scaffolding Assembly")
                .build();

        workTypeAuditableDTO = WorkTypeAuditableDTO.builder()
                .base(workTypeDTO)
                .build();

        workType = new WorkType();
        workType.setId(1L);
        workType.setName("Scaffolding Assembly");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        WorkTypeFilter filter = new WorkTypeFilter();

        when(workTypeDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(workType));
        when(workTypeAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(workTypeAuditableDTO));

        List<WorkTypeAuditableDTO> result = workTypeQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Scaffolding Assembly", result.getFirst().getBase().getName());

        verify(workTypeDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(workTypeAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        WorkTypeFilter filter = new WorkTypeFilter();

        when(workTypeDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(workTypeAuditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<WorkTypeAuditableDTO> result = workTypeQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(workTypeDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(workTypeAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        WorkTypeFilter filter = new WorkTypeFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<WorkType> workTypePage = new PageImpl<>(Collections.singletonList(workType));
        when(workTypeDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workTypePage);
        when(workTypeAuditMapper.toDto(any(WorkType.class))).thenReturn(workTypeAuditableDTO);

        Page<WorkTypeAuditableDTO> result = workTypeQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Scaffolding Assembly", result.getContent().getFirst().getBase().getName());

        verify(workTypeDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(workTypeAuditMapper, times(1)).toDto(any(WorkType.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        WorkTypeFilter filter = new WorkTypeFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<WorkType> workTypePage = Page.empty();
        when(workTypeDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workTypePage);

        Page<WorkTypeAuditableDTO> result = workTypeQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(workTypeDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(workTypeAuditMapper, never()).toDto(any(WorkType.class));
    }

    @Test
    void testFindByFilterWithCriteria() {
        WorkTypeFilter filter = WorkTypeFilter.builder()
                .nameContains("Assembly")
                .build();

        when(workTypeDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(workType));
        when(workTypeAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(workTypeAuditableDTO));

        List<WorkTypeAuditableDTO> result = workTypeQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(workTypeDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(workTypeAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterAndPageWithCriteria() {
        WorkTypeFilter filter = WorkTypeFilter.builder()
                .nameContains("Assembly")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<WorkType> workTypePage = new PageImpl<>(Collections.singletonList(workType), pageable, 1);
        when(workTypeDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(workTypePage);
        when(workTypeAuditMapper.toDto(any(WorkType.class))).thenReturn(workTypeAuditableDTO);

        Page<WorkTypeAuditableDTO> result = workTypeQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(workTypeDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(workTypeAuditMapper, times(1)).toDto(any(WorkType.class));
    }
}
