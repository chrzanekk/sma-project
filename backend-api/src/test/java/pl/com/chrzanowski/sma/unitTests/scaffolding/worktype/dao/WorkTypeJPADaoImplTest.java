package pl.com.chrzanowski.sma.unitTests.scaffolding.worktype.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.helper.SimplePagedList;
import pl.com.chrzanowski.sma.scaffolding.worktype.dao.WorkTypeJPADaoImpl;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.QWorkType;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;
import pl.com.chrzanowski.sma.scaffolding.worktype.repository.WorkTypeRepository;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkTypeJPADaoImplTest {

    @InjectMocks
    private WorkTypeJPADaoImpl workTypeJPADaoImpl;

    @Mock
    private WorkTypeRepository workTypeRepository;

    @Mock
    private WorkTypeQuerySpec workTypeQuerySpec;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_Positive() {
        WorkType workType = new WorkType();
        when(workTypeRepository.save(workType)).thenReturn(workType);

        WorkType savedWorkType = workTypeJPADaoImpl.save(workType);

        assertEquals(workType, savedWorkType);
        verify(workTypeRepository, times(1)).save(workType);
    }

    @Test
    void save_Negative() {
        WorkType workType = new WorkType();
        when(workTypeRepository.save(workType)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> workTypeJPADaoImpl.save(workType));
        verify(workTypeRepository, times(1)).save(workType);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        WorkType workType = new WorkType();
        when(workTypeRepository.findById(id)).thenReturn(Optional.of(workType));

        Optional<WorkType> foundWorkType = workTypeJPADaoImpl.findById(id);

        assertTrue(foundWorkType.isPresent());
        assertEquals(workType, foundWorkType.get());
        verify(workTypeRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(workTypeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<WorkType> foundWorkType = workTypeJPADaoImpl.findById(id);

        assertTrue(foundWorkType.isEmpty());
        verify(workTypeRepository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        WorkType workType = new WorkType();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<WorkType> mockQuery = mock(BlazeJPAQuery.class);
        when(workTypeQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(workType));

        List<WorkType> workTypes = workTypeJPADaoImpl.findAll(specification);

        assertEquals(1, workTypes.size());
        assertEquals(workType, workTypes.getFirst());
        verify(workTypeQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<WorkType> mockQuery = mock(BlazeJPAQuery.class);
        when(workTypeQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        List<WorkType> workTypes = workTypeJPADaoImpl.findAll(specification);

        assertTrue(workTypes.isEmpty());
        verify(workTypeQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        WorkType workType = new WorkType();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<WorkType> mockQuery = mock(BlazeJPAQuery.class);
        when(mockQuery.leftJoin(eq(QWorkType.workType.company), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(workTypeQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<WorkType> pagedList = new SimplePagedList<>(List.of(workType), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<WorkType> result = workTypeJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(workType, result.getContent().getFirst());
        verify(workTypeQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<WorkType> mockQuery = mock(BlazeJPAQuery.class);
        when(mockQuery.leftJoin(eq(QWorkType.workType.company), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(workTypeQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<WorkType> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<WorkType> result = workTypeJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(workTypeQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        workTypeJPADaoImpl.deleteById(id);

        verify(workTypeRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(workTypeRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> workTypeJPADaoImpl.deleteById(id));
        verify(workTypeRepository, times(1)).deleteById(id);
    }
}
