package pl.com.chrzanowski.sma.unitTests.scaffolding.log.dao;

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
import pl.com.chrzanowski.sma.scaffolding.log.dao.ScaffoldingLogDaoImpl;
import pl.com.chrzanowski.sma.scaffolding.log.model.QScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.log.repository.ScaffoldingLogRepository;
import pl.com.chrzanowski.sma.scaffolding.log.service.filter.ScaffoldingLogQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScaffoldingLogDaoImplTest {

    @InjectMocks
    private ScaffoldingLogDaoImpl scaffoldingLogDaoImpl;

    @Mock
    private ScaffoldingLogRepository scaffoldingLogRepository;

    @Mock
    private ScaffoldingLogQuerySpec scaffoldingLogQuerySpec;

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
        ScaffoldingLog scaffoldingLog = new ScaffoldingLog();
        when(scaffoldingLogRepository.save(scaffoldingLog)).thenReturn(scaffoldingLog);

        ScaffoldingLog savedScaffoldingLog = scaffoldingLogDaoImpl.save(scaffoldingLog);

        assertEquals(scaffoldingLog, savedScaffoldingLog);
        verify(scaffoldingLogRepository, times(1)).save(scaffoldingLog);
    }

    @Test
    void save_Negative() {
        ScaffoldingLog scaffoldingLog = new ScaffoldingLog();
        when(scaffoldingLogRepository.save(scaffoldingLog)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> scaffoldingLogDaoImpl.save(scaffoldingLog));
        verify(scaffoldingLogRepository, times(1)).save(scaffoldingLog);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        ScaffoldingLog scaffoldingLog = new ScaffoldingLog();
        when(scaffoldingLogRepository.findById(id)).thenReturn(Optional.of(scaffoldingLog));

        Optional<ScaffoldingLog> foundScaffoldingLog = scaffoldingLogDaoImpl.findById(id);

        assertTrue(foundScaffoldingLog.isPresent());
        assertEquals(scaffoldingLog, foundScaffoldingLog.get());
        verify(scaffoldingLogRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(scaffoldingLogRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ScaffoldingLog> foundScaffoldingLog = scaffoldingLogDaoImpl.findById(id);

        assertTrue(foundScaffoldingLog.isEmpty());
        verify(scaffoldingLogRepository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        ScaffoldingLog scaffoldingLog = new ScaffoldingLog();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLog> mockQuery = mock(BlazeJPAQuery.class);
        when(scaffoldingLogQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(scaffoldingLog));

        List<ScaffoldingLog> scaffoldingLogs = scaffoldingLogDaoImpl.findAll(specification);

        assertEquals(1, scaffoldingLogs.size());
        assertEquals(scaffoldingLog, scaffoldingLogs.getFirst());
        verify(scaffoldingLogQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLog> mockQuery = mock(BlazeJPAQuery.class);
        when(scaffoldingLogQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        List<ScaffoldingLog> scaffoldingLogs = scaffoldingLogDaoImpl.findAll(specification);

        assertTrue(scaffoldingLogs.isEmpty());
        verify(scaffoldingLogQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        ScaffoldingLog scaffoldingLog = new ScaffoldingLog();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLog> mockQuery = mock(BlazeJPAQuery.class);
        when(mockQuery.leftJoin(eq(QScaffoldingLog.scaffoldingLog.company), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(scaffoldingLogQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLog> pagedList = new SimplePagedList<>(List.of(scaffoldingLog), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<ScaffoldingLog> result = scaffoldingLogDaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(scaffoldingLog, result.getContent().getFirst());
        verify(scaffoldingLogQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLog> mockQuery = mock(BlazeJPAQuery.class);
        when(mockQuery.leftJoin(eq(QScaffoldingLog.scaffoldingLog.company), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(scaffoldingLogQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLog> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<ScaffoldingLog> result = scaffoldingLogDaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(scaffoldingLogQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        scaffoldingLogDaoImpl.deleteById(id);

        verify(scaffoldingLogRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(scaffoldingLogRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> scaffoldingLogDaoImpl.deleteById(id));
        verify(scaffoldingLogRepository, times(1)).deleteById(id);
    }
}
