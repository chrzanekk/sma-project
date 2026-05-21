package pl.com.chrzanowski.sma.unitTests.scaffolding.dimension.dao;

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
import pl.com.chrzanowski.sma.scaffolding.dimension.dao.ScaffoldingLogPositionDimensionDaoImpl;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.QScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.dimension.model.ScaffoldingLogPositionDimension;
import pl.com.chrzanowski.sma.scaffolding.dimension.repository.ScaffoldingLogPositionDimensionRepository;
import pl.com.chrzanowski.sma.scaffolding.dimension.service.filter.ScaffoldingLogPositionDimensionQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScaffoldingLogPositionDimensionDaoImplTest {

    @InjectMocks
    private ScaffoldingLogPositionDimensionDaoImpl dimensionDaoImpl;

    @Mock
    private ScaffoldingLogPositionDimensionRepository repository;

    @Mock
    private ScaffoldingLogPositionDimensionQuerySpec querySpec;

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
        ScaffoldingLogPositionDimension dimension = new ScaffoldingLogPositionDimension();
        when(repository.save(dimension)).thenReturn(dimension);

        ScaffoldingLogPositionDimension savedDimension = dimensionDaoImpl.save(dimension);

        assertEquals(dimension, savedDimension);
        verify(repository, times(1)).save(dimension);
    }

    @Test
    void save_Negative() {
        ScaffoldingLogPositionDimension dimension = new ScaffoldingLogPositionDimension();
        when(repository.save(dimension)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> dimensionDaoImpl.save(dimension));
        verify(repository, times(1)).save(dimension);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        ScaffoldingLogPositionDimension dimension = new ScaffoldingLogPositionDimension();
        when(repository.findById(id)).thenReturn(Optional.of(dimension));

        Optional<ScaffoldingLogPositionDimension> foundDimension = dimensionDaoImpl.findById(id);

        assertTrue(foundDimension.isPresent());
        assertEquals(dimension, foundDimension.get());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<ScaffoldingLogPositionDimension> foundDimension = dimensionDaoImpl.findById(id);

        assertTrue(foundDimension.isEmpty());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        ScaffoldingLogPositionDimension dimension = new ScaffoldingLogPositionDimension();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionDimension> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(dimension));

        List<ScaffoldingLogPositionDimension> dimensions = dimensionDaoImpl.findAll(specification);

        assertEquals(1, dimensions.size());
        assertEquals(dimension, dimensions.getFirst());
        verify(querySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionDimension> mockQuery = mock(BlazeJPAQuery.class);
        when(querySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        List<ScaffoldingLogPositionDimension> dimensions = dimensionDaoImpl.findAll(specification);

        assertTrue(dimensions.isEmpty());
        verify(querySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        ScaffoldingLogPositionDimension dimension = new ScaffoldingLogPositionDimension();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionDimension> mockQuery = mock(BlazeJPAQuery.class);
        when(mockQuery.leftJoin(eq(QScaffoldingLogPositionDimension.scaffoldingLogPositionDimension.company), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(querySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLogPositionDimension> pagedList = new SimplePagedList<>(List.of(dimension), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<ScaffoldingLogPositionDimension> result = dimensionDaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(dimension, result.getContent().getFirst());
        verify(querySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<ScaffoldingLogPositionDimension> mockQuery = mock(BlazeJPAQuery.class);
        when(mockQuery.leftJoin(eq(QScaffoldingLogPositionDimension.scaffoldingLogPositionDimension.company), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(querySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<ScaffoldingLogPositionDimension> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<ScaffoldingLogPositionDimension> result = dimensionDaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(querySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        dimensionDaoImpl.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(repository).deleteById(id);

        assertThrows(RuntimeException.class, () -> dimensionDaoImpl.deleteById(id));
        verify(repository, times(1)).deleteById(id);
    }
}
