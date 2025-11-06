package pl.com.chrzanowski.sma.unitTests.employee.dao;

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
import pl.com.chrzanowski.sma.employee.dao.EmployeeDaoImpl;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.employee.repository.EmployeeRepository;
import pl.com.chrzanowski.sma.employee.service.filter.EmployeeQuerySpec;
import pl.com.chrzanowski.sma.helper.SimplePagedList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EmployeeDaoImplTest {

    @InjectMocks
    private EmployeeDaoImpl employeeDaoImpl;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeQuerySpec employeeQuerySpec;

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
        Employee employee = new Employee();
        when(employeeRepository.save(employee)).thenReturn(employee);
        assertEquals(employee, employeeDaoImpl.save(employee));
        verify(employeeRepository).save(employee);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        assertEquals(Optional.of(employee), employeeDaoImpl.findById(id));
        verify(employeeRepository).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(employeeDaoImpl.findById(id).isEmpty());
        verify(employeeRepository).findById(id);
    }

    @Test
    void findAll_Positive() {
        Employee employee = new Employee();
        List<Employee> employees = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        assertEquals(employees, employeeDaoImpl.findAll());
        verify(employeeRepository).findAll();
    }

    @Test
    void findAllWithSpecification_Positive() {
        BooleanBuilder spec = new BooleanBuilder();
        Employee employee = new Employee();
        List<Employee> employees = List.of(employee);

        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Employee> query = mock(BlazeJPAQuery.class);
        when(employeeQuerySpec.buildQuery(spec, null)).thenReturn(query);
        when(query.fetch()).thenReturn(employees);

        List<Employee> result = employeeDaoImpl.findAll(spec);
        assertEquals(1, result.size());
        verify(employeeQuerySpec).buildQuery(spec, null);
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        // Given
        BooleanBuilder spec = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee();

        // Mock query z RETURNS_DEEP_STUBS aby obsłużyć fluent API
        @SuppressWarnings("unchecked")
        BlazeJPAQuery<Employee> query = mock(BlazeJPAQuery.class, RETURNS_DEEP_STUBS);

        when(employeeQuerySpec.buildQuery(spec, pageable)).thenReturn(query);

        // Mock fetchPage - to jest ostateczne wywołanie w łańcuchu
        PagedList<Employee> pagedList = new SimplePagedList<>(List.of(employee), 1);
        when(query.fetchPage(anyInt(), anyInt())).thenReturn(pagedList);

        // When
        Page<Employee> result = employeeDaoImpl.findAll(spec, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(employee, result.getContent().get(0));

        verify(employeeQuerySpec).buildQuery(spec, pageable);
        verify(query).fetchPage(0, 10);
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;
        employeeDaoImpl.deleteById(id);
        verify(employeeRepository).deleteById(id);
    }
}
