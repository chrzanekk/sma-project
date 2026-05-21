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
import pl.com.chrzanowski.sma.scaffolding.position.dao.ScaffoldingLogPositionDaoImpl;
import pl.com.chrzanowski.sma.scaffolding.position.model.QScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.scaffolding.position.repository.ScaffoldingLogPositionRepository;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionDaoImplTest {

    @InjectMocks
    private ScaffoldingLogPositionDaoImpl scaffoldingLogPositionDaoImpl;

    @Mock
    private ScaffoldingLogPositionRepository scaffoldingLogPositionRepository;

    @Mock
    private ScaffoldingLogPositionQuerySpec scaffoldingLogPositionQuerySpec;

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
        ScaffoldingLogPosition position = new ScaffoldingLogPosition();
        when(scaffoldingLogPositionRepository.save(position)).thenReturn(position);

        ScaffoldingLogPosition saved = scaffoldingLogPositionDaoImpl.save(position);

        assertEquals(position, saved);
        verify(scaffoldingLogPositionRepository, times(1)).save(position);
    }

    @Test
    void save_Negative() {
        ScaffoldingLogPosition position = new ScaffoldingLogPosition();
        when(scaffoldingLogPositionRepository.save(position)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> scaffoldingLogPositionDaoImpl.save(position));
        verify(scaffoldingLogPositionRepository, times(1)).save(position);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        ScaffoldingLogPosition position = new ScaffoldingLogPosition();
        when(scaffoldingLogPositionRepository.findById(id)).thenReturn(Optional.of(position));

        Optional<ScaffoldingLogPosition> found = scaffoldingLogPositionDaoImpl.findById(id);

        assertTrue(found.isPresent());
        assertEquals(position, found.get());
        verify(scaffoldingLogPositionRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(scaffoldingLogPositionRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ScaffoldingLogPosition> found = scaffoldingLogPositionDaoImpl.findById(id);

        assertTrue(found.isEmpty());
        verify(scaffoldingLogPositionRepository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        ScaffoldingLogPosition position = new ScaffoldingLogPosition();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPosition> mockQuery = mock(BlazeJPAQuery.class);
        when(scaffoldingLogPositionQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(position));

        List<ScaffoldingLogPosition> list = scaffoldingLogPositionDaoImpl.findAll(specification);

        assertEquals(1, list.size());
        assertEquals(position, list.get(0));
        verify(scaffoldingLogPositionQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPosition> mockQuery = mock(BlazeJPAQuery.class);
        when(scaffoldingLogPositionQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        List<ScaffoldingLogPosition> list = scaffoldingLogPositionDaoImpl.findAll(specification);

        assertTrue(list.isEmpty());
        verify(scaffoldingLogPositionQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        ScaffoldingLogPosition position = new ScaffoldingLogPosition();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPosition> mockQuery = mock(BlazeJPAQuery.class);
        when(scaffoldingLogPositionQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLogPosition> pagedList = new SimplePagedList<>(List.of(position), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        Page<ScaffoldingLogPosition> result = scaffoldingLogPositionDaoImpl.findAll(specification, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(position, result.getContent().get(0));
        verify(scaffoldingLogPositionQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPosition> mockQuery = mock(BlazeJPAQuery.class);
        when(scaffoldingLogPositionQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLogPosition> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        Page<ScaffoldingLogPosition> result = scaffoldingLogPositionDaoImpl.findAll(specification, pageable);

        assertTrue(result.isEmpty());
        verify(scaffoldingLogPositionQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        scaffoldingLogPositionDaoImpl.deleteById(id);

        verify(scaffoldingLogPositionRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(scaffoldingLogPositionRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> scaffoldingLogPositionDaoImpl.deleteById(id));
        verify(scaffoldingLogPositionRepository, times(1)).deleteById(id);
    }
}
