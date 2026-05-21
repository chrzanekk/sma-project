package pl.com.chrzanowski.sma.unitTests.scaffolding.workingtime.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
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
import pl.com.chrzanowski.sma.scaffolding.workingtime.dao.ScaffoldingLogPositionWorkingTimeJPADaoImpl;
import pl.com.chrzanowski.sma.scaffolding.workingtime.model.ScaffoldingLogPositionWorkingTime;
import pl.com.chrzanowski.sma.scaffolding.workingtime.repository.ScaffoldingLogPositionWorkingTimeRepository;
import pl.com.chrzanowski.sma.scaffolding.workingtime.service.filter.ScaffoldingLogPositionWorkingTimeQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionWorkingTimeJPADaoImplTest {

    @InjectMocks
    private ScaffoldingLogPositionWorkingTimeJPADaoImpl scaffoldingLogPositionWorkingTimeJPADaoImpl;

    @Mock
    private ScaffoldingLogPositionWorkingTimeRepository repository;

    @Mock
    private ScaffoldingLogPositionWorkingTimeQuerySpec querySpec;

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
        ScaffoldingLogPositionWorkingTime workingTime = new ScaffoldingLogPositionWorkingTime();
        when(repository.save(workingTime)).thenReturn(workingTime);

        ScaffoldingLogPositionWorkingTime savedWorkingTime = scaffoldingLogPositionWorkingTimeJPADaoImpl.save(workingTime);

        assertEquals(workingTime, savedWorkingTime);
        verify(repository, times(1)).save(workingTime);
    }

    @Test
    void save_Negative() {
        ScaffoldingLogPositionWorkingTime workingTime = new ScaffoldingLogPositionWorkingTime();
        when(repository.save(workingTime)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> scaffoldingLogPositionWorkingTimeJPADaoImpl.save(workingTime));
        verify(repository, times(1)).save(workingTime);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        ScaffoldingLogPositionWorkingTime workingTime = new ScaffoldingLogPositionWorkingTime();
        when(repository.findById(id)).thenReturn(Optional.of(workingTime));

        Optional<ScaffoldingLogPositionWorkingTime> foundWorkingTime = scaffoldingLogPositionWorkingTimeJPADaoImpl.findById(id);

        assertTrue(foundWorkingTime.isPresent());
        assertEquals(workingTime, foundWorkingTime.get());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<ScaffoldingLogPositionWorkingTime> foundWorkingTime = scaffoldingLogPositionWorkingTimeJPADaoImpl.findById(id);

        assertTrue(foundWorkingTime.isEmpty());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        ScaffoldingLogPositionWorkingTime workingTime = new ScaffoldingLogPositionWorkingTime();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(workingTime));

        List<ScaffoldingLogPositionWorkingTime> workingTimes = scaffoldingLogPositionWorkingTimeJPADaoImpl.findAll(specification);

        assertEquals(1, workingTimes.size());
        assertEquals(workingTime, workingTimes.getFirst());
        verify(querySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        List<ScaffoldingLogPositionWorkingTime> workingTimes = scaffoldingLogPositionWorkingTimeJPADaoImpl.findAll(specification);

        assertTrue(workingTimes.isEmpty());
        verify(querySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        ScaffoldingLogPositionWorkingTime workingTime = new ScaffoldingLogPositionWorkingTime();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> mockQuery = mock(BlazeJPAQuery.class);

        when(querySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLogPositionWorkingTime> pagedList = new SimplePagedList<>(List.of(workingTime), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<ScaffoldingLogPositionWorkingTime> result = scaffoldingLogPositionWorkingTimeJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(workingTime, result.getContent().getFirst());
        verify(querySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionWorkingTime> mockQuery = mock(BlazeJPAQuery.class);

        when(querySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLogPositionWorkingTime> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<ScaffoldingLogPositionWorkingTime> result = scaffoldingLogPositionWorkingTimeJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(querySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        scaffoldingLogPositionWorkingTimeJPADaoImpl.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(repository).deleteById(id);

        assertThrows(RuntimeException.class, () -> scaffoldingLogPositionWorkingTimeJPADaoImpl.deleteById(id));
        verify(repository, times(1)).deleteById(id);
    }
}
