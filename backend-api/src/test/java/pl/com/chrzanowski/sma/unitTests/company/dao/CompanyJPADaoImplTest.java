package pl.com.chrzanowski.sma.unitTests.company.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
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
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.company.dao.CompanyJPADaoImpl;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.company.service.filter.CompanyQuerySpec;
import pl.com.chrzanowski.sma.helper.SimplePagedList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompanyJPADaoImplTest {

    @InjectMocks
    private CompanyJPADaoImpl companyJPADaoImpl;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyQuerySpec companyQuerySpec;

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
    void findAll_Positive() {
        //Given
        Company firstCompany = Company.builder().name("First Company").build();
        Company secondCompany = Company.builder().name("Second Company").build();
        when(companyRepository.findAll()).thenReturn(List.of(firstCompany, secondCompany));

        //When
        List<Company> companies = companyJPADaoImpl.findAll();

        //Then
        assertEquals(2, companies.size());
        assertTrue(companies.contains(firstCompany));
        assertTrue(companies.contains(secondCompany));
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void findAll_EmptyList() {
        //Given
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());

        //When
        List<Company> companies = companyJPADaoImpl.findAll();

        //Then
        assertTrue(companies.isEmpty());
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void findByName_Positive() {
        //Given
        Company secondCompany = Company.builder().name("Second Company").build();
        when(companyRepository.findByName(secondCompany.getName())).thenReturn(Optional.of(secondCompany));

        //When
        Optional<Company> company = companyJPADaoImpl.findByName(secondCompany.getName());

        //Then
        assertTrue(company.isPresent());
        assertEquals(secondCompany, company.get());
        verify(companyRepository, times(1)).findByName(secondCompany.getName());
    }

    @Test
    void findByName_Negative() {
        //Given
        String companyName = "First Company";
        when(companyRepository.findByName(companyName)).thenReturn(Optional.empty());

        //When
        Optional<Company> company = companyJPADaoImpl.findByName(companyName);

        //Then
        assertTrue(company.isEmpty());
        verify(companyRepository, times(1)).findByName(companyName);
    }

    @Test
    void saveCompany_Positive() {
        //Given
        Company firstCompany = Company.builder().name("First Company").build();
        Company savedCompany = Company.builder().id(1L).name(firstCompany.getName()).build();
        when(companyRepository.save(firstCompany)).thenReturn(savedCompany);

        //When
        Company returnedCompany = companyJPADaoImpl.save(firstCompany);

        //Then
        assertEquals(returnedCompany, savedCompany);
        verify(companyRepository, times(1)).save(firstCompany);
    }

    @Test
    void saveCompany_Negative() {
        //Given
        Company firstCompany = Company.builder().name("First Company").build();
        when(companyRepository.save(firstCompany)).thenThrow(new RuntimeException("Save failed"));

        //When / Then
        assertThrows(RuntimeException.class, () -> companyJPADaoImpl.save(firstCompany));
        verify(companyRepository, times(1)).save(firstCompany);
    }

    @Test
    void deleteCompany_Positive() {
        Long companyId = 1L;

        assertDoesNotThrow(() -> companyJPADaoImpl.deleteById(companyId));

        verify(companyRepository, times(1)).deleteById(companyId);
    }

    @Test
    void findById_Positive() {
        //Given
        Long companyId = 1L;
        Company company = Company.builder().id(companyId).name("First Company").build();
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        //When
        Optional<Company> companyOptional = companyJPADaoImpl.findById(companyId);

        //Then
        assertTrue(companyOptional.isPresent());
        assertEquals(company, companyOptional.get());
        verify(companyRepository, times(1)).findById(companyId);
    }

    @Test
    void findById_Negative() {
        //Given
        Long companyId = 999L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        //When
        Optional<Company> companyOptional = companyJPADaoImpl.findById(companyId);

        //Then
        assertTrue(companyOptional.isEmpty());
        verify(companyRepository, times(1)).findById(companyId);
    }

    @Test
    void findAll_withSpecAndPage_Positive() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        Company company = Company.builder().id(1L).name("First Company").build();
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Company> mockQuery = mock(BlazeJPAQuery.class);
        @SuppressWarnings("unchecked")
        EntityGraph<Company> mockEntityGraph = mock(EntityGraph.class);
        when(em.createEntityGraph(Company.class)).thenReturn(mockEntityGraph);

        when(companyQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<Company> pagedList = new SimplePagedList<>(List.of(company), 1);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Company> page = companyJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getContent().size());
        assertTrue(page.getContent().contains(company));
        verify(companyQuerySpec, times(1)).buildQuery(specification, pageable);
        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAll_withSpecAndPage_Negative() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        BooleanBuilder specification = new BooleanBuilder();

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Company> mockQuery = mock(BlazeJPAQuery.class);
        @SuppressWarnings("unchecked")
        EntityGraph<Company> mockEntityGraph = mock(EntityGraph.class);
        when(em.createEntityGraph(Company.class)).thenReturn(mockEntityGraph);

        when(companyQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);

        PagedList<Company> pagedList = new SimplePagedList<>(Collections.emptyList(), 0);
        when(mockQuery.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Company> page = companyJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(page.isEmpty());
        verify(companyQuerySpec, times(1)).buildQuery(specification, pageable);

        verify(mockQuery, times(1)).fetchPage((int) pageable.getOffset(), pageable.getPageSize());
    }

    @Test
    void findAll_withSpec_Positive() {
        //Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        BlazeJPAQuery<Company> query = mock(BlazeJPAQuery.class);
        Company company = Company.builder().id(1L).name("First Company").build();

        when(companyQuerySpec.buildQuery(specification, null)).thenReturn(query);
        when(query.fetch()).thenReturn(List.of(company));

        //When
        List<Company> companies = companyJPADaoImpl.findAll(specification);

        //Then
        assertEquals(1, companies.size());
        assertTrue(companies.contains(company));
        verify(companyQuerySpec, times(1)).buildQuery(specification, null);
        verify(query, times(1)).fetch();
    }

    @Test
    void findAll_withSpec_Negative() {
        //Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        BlazeJPAQuery<Company> query = mock(BlazeJPAQuery.class);

        when(companyQuerySpec.buildQuery(specification, null)).thenReturn(query);
        when(query.fetch()).thenReturn(Collections.emptyList());

        //When
        List<Company> companies = companyJPADaoImpl.findAll(specification);

        //Then

        assertTrue(companies.isEmpty());
        verify(companyQuerySpec, times(1)).buildQuery(specification, null);
        verify(query, times(1)).fetch();
    }

}
