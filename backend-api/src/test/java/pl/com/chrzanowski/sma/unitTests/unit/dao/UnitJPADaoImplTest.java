package pl.com.chrzanowski.sma.unitTests.unit.dao;

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
import pl.com.chrzanowski.sma.unit.dao.UnitJPADaoImpl;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.unit.repository.UnitRepository;
import pl.com.chrzanowski.sma.unit.service.filter.UnitQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitJPADaoImplTest {

    @InjectMocks
    private UnitJPADaoImpl unitJPADaoImpl;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UnitQuerySpec unitQuerySpec;

    @Mock
    private BlazeJPAQuery<Unit> baseQuery;

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
        Unit unit = new Unit();
        unit.setSymbol("Test Position");
        when(unitRepository.save(unit)).thenReturn(unit);

        Unit savedPosition = unitJPADaoImpl.save(unit);

        assertEquals(unit, savedPosition);
        verify(unitRepository, times(1)).save(unit);
    }

    @Test
    void save_Negative() {
        Unit position = new Unit();
        when(unitRepository.save(position)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> unitJPADaoImpl.save(position));
        verify(unitRepository, times(1)).save(position);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        Unit position = new Unit();
        position.setSymbol("Test Position");
        when(unitRepository.findById(id)).thenReturn(Optional.of(position));

        Optional<Unit> foundPosition = unitJPADaoImpl.findById(id);

        assertTrue(foundPosition.isPresent());
        assertEquals(position, foundPosition.get());
        verify(unitRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(unitRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Unit> foundPosition = unitJPADaoImpl.findById(id);

        assertTrue(foundPosition.isEmpty());
        verify(unitRepository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        Unit unit = new Unit();
        unit.setSymbol("Test Position");

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Unit> mockQuery = mock(BlazeJPAQuery.class);

        when(unitQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<Unit> pagedList = new SimplePagedList<>(List.of(unit), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Unit> result = unitJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(unit, result.getContent().getFirst());
        verify(unitQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Unit> mockQuery = mock(BlazeJPAQuery.class);

        when(unitQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<Unit> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Unit> result = unitJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(unitQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecification_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Unit unit = new Unit();
        unit.setSymbol("Test Position");

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Unit> mockQuery = mock(BlazeJPAQuery.class);

        when(unitQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(unit));

        // When
        List<Unit> result = unitJPADaoImpl.findAll(specification);

        // Then
        assertEquals(1, result.size());
        assertEquals(unit, result.getFirst());
        verify(unitQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Unit> mockQuery = mock(BlazeJPAQuery.class);

        when(unitQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        // When
        List<Unit> result = unitJPADaoImpl.findAll(specification);

        // Then
        assertTrue(result.isEmpty());
        verify(unitQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        unitJPADaoImpl.deleteById(id);

        verify(unitRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(unitRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> unitJPADaoImpl.deleteById(id));
        verify(unitRepository, times(1)).deleteById(id);
    }
}
