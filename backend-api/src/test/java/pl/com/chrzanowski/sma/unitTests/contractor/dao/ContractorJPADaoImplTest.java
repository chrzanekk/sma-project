package pl.com.chrzanowski.sma.unitTests.contractor.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.com.chrzanowski.sma.contractor.dao.ContractorJPADaoImpl;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.repository.ContractorRepository;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractorJPADaoImplTest {

    @InjectMocks
    private ContractorJPADaoImpl contractorJPADaoImpl;

    @Mock
    private ContractorRepository contractorRepository;

    @Mock
    private ContractorQuerySpec querySpec;

    @Mock
    private EntityManager em;

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
        // Given
        Contractor contractor = Contractor.builder().name("Test Contractor").build();
        when(contractorRepository.save(contractor)).thenReturn(contractor);

        // When
        Contractor savedContractor = contractorJPADaoImpl.save(contractor);

        // Then
        assertEquals(contractor, savedContractor);
        verify(contractorRepository, times(1)).save(contractor);
    }

    @Test
    void save_Negative() {
        // Given
        Contractor contractor = Contractor.builder().name("Test Contractor").build();
        when(contractorRepository.save(contractor)).thenThrow(new RuntimeException("Save failed"));

        // When / Then
        assertThrows(RuntimeException.class, () -> contractorJPADaoImpl.save(contractor));
        verify(contractorRepository, times(1)).save(contractor);
    }

    @Test
    void findByName_Positive() {
        // Given
        String name = "Test Contractor";
        Contractor contractor = Contractor.builder().name(name).build();
        when(contractorRepository.findByName(name)).thenReturn(Optional.of(contractor));

        // When
        Optional<Contractor> foundContractor = contractorJPADaoImpl.findByName(name);

        // Then
        assertTrue(foundContractor.isPresent());
        assertEquals(contractor, foundContractor.get());
        verify(contractorRepository, times(1)).findByName(name);
    }

    @Test
    void findByName_Negative() {
        // Given
        String name = "Nonexistent Contractor";
        when(contractorRepository.findByName(name)).thenReturn(Optional.empty());

        // When
        Optional<Contractor> foundContractor = contractorJPADaoImpl.findByName(name);

        // Then
        assertTrue(foundContractor.isEmpty());
        verify(contractorRepository, times(1)).findByName(name);
    }

    @Test
    void findById_Positive() {
        // Given
        Long id = 1L;
        Contractor contractor = Contractor.builder().id(id).build();
        when(contractorRepository.findById(id)).thenReturn(Optional.of(contractor));

        // When
        Optional<Contractor> foundContractor = contractorJPADaoImpl.findById(id);

        // Then
        assertTrue(foundContractor.isPresent());
        assertEquals(contractor, foundContractor.get());
        verify(contractorRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        // Given
        Long id = 999L;
        when(contractorRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Contractor> foundContractor = contractorJPADaoImpl.findById(id);

        // Then
        assertTrue(foundContractor.isEmpty());
        verify(contractorRepository, times(1)).findById(id);
    }

    @Test
    void existsByName_Positive() {
        // Given
        String name = "Existing Contractor";
        when(contractorRepository.existsByName(name)).thenReturn(true);

        // When
        Boolean exists = contractorJPADaoImpl.existsByName(name);

        // Then
        assertTrue(exists);
        verify(contractorRepository, times(1)).existsByName(name);
    }

    @Test
    void existsByName_Negative() {
        // Given
        String name = "Nonexistent Contractor";
        when(contractorRepository.existsByName(name)).thenReturn(false);

        // When
        Boolean exists = contractorJPADaoImpl.existsByName(name);

        // Then
        assertFalse(exists);
        verify(contractorRepository, times(1)).existsByName(name);
    }

    @Test
    void findAll_Positive() {
        // Given
        Contractor contractor = Contractor.builder().name("Test Contractor").build();
        when(contractorRepository.findAll()).thenReturn(List.of(contractor));

        // When
        List<Contractor> contractors = contractorJPADaoImpl.findAll();

        // Then
        assertEquals(1, contractors.size());
        assertEquals(contractor, contractors.getFirst());
        verify(contractorRepository, times(1)).findAll();
    }

    @Test
    void findAll_Negative() {
        // Given
        when(contractorRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Contractor> contractors = contractorJPADaoImpl.findAll();

        // Then
        assertTrue(contractors.isEmpty());
        verify(contractorRepository, times(1)).findAll();
    }

    @Test
    void deleteById_Positive() {
        // Given
        Long id = 1L;

        // When
        contractorJPADaoImpl.deleteById(id);

        // Then
        verify(contractorRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(contractorRepository).deleteById(id);

        // When / Then
        assertThrows(RuntimeException.class, () -> contractorJPADaoImpl.deleteById(id));
        verify(contractorRepository, times(1)).deleteById(id);
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder(); // używamy realnego obiektu
        PageRequest pageable = PageRequest.of(0, 10);
        Contractor contractor = Contractor.builder().name("Test Contractor").build();

        @SuppressWarnings("unchecked")
        JPAQuery<Contractor> mockQuery = mock(JPAQuery.class);
        @SuppressWarnings("unchecked")
        EntityGraph<Contractor> mockEntityGraph = mock(EntityGraph.class);
        when(em.createEntityGraph(Contractor.class)).thenReturn(mockEntityGraph);

        when(querySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);
        when(mockQuery.fetchCount()).thenReturn(1L);
        when(mockQuery.setHint("jakarta.persistence.fetchgraph", mockEntityGraph)).thenReturn(mockQuery);
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(contractor));

        // When
        Page<Contractor> result = contractorJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(contractor, result.getContent().getFirst());
        verify(querySpec, times(1)).buildQuery(specification, pageable);
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        PageRequest pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        JPAQuery<Contractor> mockQuery = mock(JPAQuery.class);
        @SuppressWarnings("unchecked")
        EntityGraph<Contractor> mockEntityGraph = mock(EntityGraph.class);
        when(em.createEntityGraph(Contractor.class)).thenReturn(mockEntityGraph);

        when(querySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);
        when(mockQuery.setHint("jakarta.persistence.fetchgraph", mockEntityGraph)).thenReturn(mockQuery);
        when(mockQuery.fetchCount()).thenReturn(0L);
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        // When
        Page<Contractor> result = contractorJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(querySpec, times(1)).buildQuery(specification, pageable);
    }

    @Test
    void findAllWithSpecification_Positive() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();
        Contractor contractor = Contractor.builder().name("Test Contractor").build();

        @SuppressWarnings("unchecked")
        JPQLQuery<Contractor> mockQuery = mock(JPQLQuery.class);

        when(querySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(List.of(contractor));

        // When
        List<Contractor> result = contractorJPADaoImpl.findAll(specification);

        // Then
        assertEquals(1, result.size());
        assertEquals(contractor, result.getFirst());
        verify(querySpec, times(1)).buildQuery(specification, null);
    }

    @Test
    void findAllWithSpecification_Negative() {
        // Given
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        JPQLQuery<Contractor> mockQuery = mock(JPQLQuery.class);

        when(querySpec.buildQuery(specification, null)).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        // When
        List<Contractor> result = contractorJPADaoImpl.findAll(specification);

        // Then
        assertTrue(result.isEmpty());
        verify(querySpec, times(1)).buildQuery(specification, null);
    }
}
