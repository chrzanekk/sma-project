package pl.com.chrzanowski.sma.unitTests.position.dao;

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
import pl.com.chrzanowski.sma.position.dao.PositionJPADaoImpl;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.repository.PositionRepository;
import pl.com.chrzanowski.sma.position.service.filter.PositionQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositionJPADaoImplTest {

    @InjectMocks
    private PositionJPADaoImpl positionJPADaoImpl;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionQuerySpec positionQuerySpec;

    @Mock
    private BlazeJPAQuery<Position> baseQuery;

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
        Position position = new Position();
        position.setName("Test Position");
        when(positionRepository.save(position)).thenReturn(position);

        Position savedPosition = positionJPADaoImpl.save(position);

        assertEquals(position, savedPosition);
        verify(positionRepository, times(1)).save(position);
    }

    @Test
    void save_Negative() {
        Position position = new Position();
        when(positionRepository.save(position)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> positionJPADaoImpl.save(position));
        verify(positionRepository, times(1)).save(position);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        Position position = new Position();
        position.setName("Test Position");
        when(positionRepository.findById(id)).thenReturn(Optional.of(position));

        Optional<Position> foundPosition = positionJPADaoImpl.findById(id);

        assertTrue(foundPosition.isPresent());
        assertEquals(position, foundPosition.get());
        verify(positionRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(positionRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Position> foundPosition = positionJPADaoImpl.findById(id);

        assertTrue(foundPosition.isEmpty());
        verify(positionRepository, times(1)).findById(id);
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        Position position = new Position();
        position.setName("Test Position");

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Position> mockQuery = mock(BlazeJPAQuery.class);

        when(positionQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<Position> pagedList = new SimplePagedList<>(List.of(position), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Position> result = positionJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(position, result.getContent().getFirst());
        verify(positionQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Position> mockQuery = mock(BlazeJPAQuery.class);

        when(positionQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<Position> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Position> result = positionJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(positionQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAllWithSpecification_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Position position = new Position();
        position.setName("Test Position");

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Position> mockQuery = mock(BlazeJPAQuery.class);

        when(positionQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(position));

        // When
        List<Position> result = positionJPADaoImpl.findAll(specification);

        // Then
        assertEquals(1, result.size());
        assertEquals(position, result.getFirst());
        verify(positionQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void findAllWithSpecification_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Position> mockQuery = mock(BlazeJPAQuery.class);

        when(positionQuerySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        // When
        List<Position> result = positionJPADaoImpl.findAll(specification);

        // Then
        assertTrue(result.isEmpty());
        verify(positionQuerySpec, times(1)).buildQuery(specification, null);
        verify(mockQuery, times(1)).fetch();
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        positionJPADaoImpl.deleteById(id);

        verify(positionRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(positionRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> positionJPADaoImpl.deleteById(id));
        verify(positionRepository, times(1)).deleteById(id);
    }
}
