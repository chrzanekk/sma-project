package pl.com.chrzanowski.sma.unitTests.employee.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.common.exception.EmployeeException;
import pl.com.chrzanowski.sma.common.exception.PositionException;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.employee.dao.EmployeeDao;
import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
import pl.com.chrzanowski.sma.employee.mapper.EmployeeDTOMapper;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.employee.service.EmployeeServiceImpl;
import pl.com.chrzanowski.sma.position.dao.PositionDao;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.model.Position;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeDao employeeDao;

    @Mock
    private EmployeeDTOMapper employeeDTOMapper;

    @Mock
    private CompanyDao companyDao;

    @Mock
    private PositionDao positionDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeDTO employeeDTO;
    private Employee employee;
    private Company company;
    private Position position;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        CompanyDTO companyDTO = CompanyDTO.builder()
                .id(1L)
                .name("Test Company")
                .build();

        PositionDTO positionDTO = PositionDTO.builder()
                .id(1L)
                .name("Software Developer")
                .build();

        employeeDTO = EmployeeDTO.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .hourRate(new BigDecimal("150.00"))
                .company(companyDTO)
                .position(positionDTO)
                .build();

        company = new Company();
        company.setId(1L);
        company.setName("Test Company");

        position = new Position();
        position.setId(1L);
        position.setName("Software Developer");

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jan");
        employee.setLastName("Kowalski");
        employee.setHourRate(new BigDecimal("150.00"));
        employee.setCompany(company);
        employee.setPosition(position);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveEmployee() {
        when(employeeDTOMapper.toEntity(any(EmployeeDTO.class))).thenReturn(employee);
        when(employeeDao.save(any(Employee.class))).thenReturn(employee);
        when(employeeDTOMapper.toDto(any(Employee.class))).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.save(employeeDTO);

        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());
        assertEquals(new BigDecimal("150.00"), result.getHourRate());
        assertEquals(1L, result.getCompany().getId());
        assertEquals(1L, result.getPosition().getId());

        verify(employeeDao, times(1)).save(any(Employee.class));
        verify(employeeDTOMapper, times(1)).toDto(any(Employee.class));
    }

    @Test
    void testSaveEmployeeFailure() {
        when(employeeDTOMapper.toEntity(any(EmployeeDTO.class))).thenReturn(employee);
        when(employeeDao.save(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> employeeService.save(employeeDTO));
        verify(employeeDao, times(1)).save(any(Employee.class));
    }

    @Test
    void testFindById() {
        when(employeeDao.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeDTOMapper.toDto(any(Employee.class))).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.findById(1L);

        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());

        verify(employeeDao, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(employeeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeException.class, () -> employeeService.findById(1L));
        verify(employeeDao, times(1)).findById(1L);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeDao.findById(anyLong())).thenReturn(Optional.of(employee));
        doNothing().when(employeeDTOMapper).updateFromDto(any(EmployeeDTO.class), any(Employee.class));
        when(companyDao.findById(anyLong())).thenReturn(Optional.of(company));
        when(positionDao.findById(anyLong())).thenReturn(Optional.of(position));
        when(employeeDao.save(any(Employee.class))).thenReturn(employee);
        when(employeeDTOMapper.toDto(any(Employee.class))).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.update(employeeDTO);

        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());

        verify(employeeDao, times(1)).findById(anyLong());
        verify(companyDao, times(1)).findById(anyLong());
        verify(positionDao, times(1)).findById(anyLong());
        verify(employeeDao, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployeeNotFound() {
        when(employeeDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EmployeeException.class, () -> employeeService.update(employeeDTO));
        verify(employeeDao, times(1)).findById(anyLong());
        verify(employeeDao, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployeeCompanyNotFound() {
        when(employeeDao.findById(anyLong())).thenReturn(Optional.of(employee));
        doNothing().when(employeeDTOMapper).updateFromDto(any(EmployeeDTO.class), any(Employee.class));
        when(companyDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CompanyException.class, () -> employeeService.update(employeeDTO));
        verify(employeeDao, times(1)).findById(anyLong());
        verify(companyDao, times(1)).findById(anyLong());
        verify(employeeDao, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployeePositionNotFound() {
        when(employeeDao.findById(anyLong())).thenReturn(Optional.of(employee));
        doNothing().when(employeeDTOMapper).updateFromDto(any(EmployeeDTO.class), any(Employee.class));
        when(companyDao.findById(anyLong())).thenReturn(Optional.of(company));
        when(positionDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PositionException.class, () -> employeeService.update(employeeDTO));
        verify(employeeDao, times(1)).findById(anyLong());
        verify(companyDao, times(1)).findById(anyLong());
        verify(positionDao, times(1)).findById(anyLong());
        verify(employeeDao, times(0)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        Long id = 1L;
        when(employeeDao.findById(id)).thenReturn(Optional.of(employee));
        doNothing().when(employeeDao).deleteById(id);

        employeeService.delete(1L);

        verify(employeeDao, times(1)).findById(id);
        verify(employeeDao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteEmployeeFailure() {
        Long id = 1L;
        when(employeeDao.findById(id)).thenReturn(Optional.of(employee));
        doThrow(new RuntimeException("Database error")).when(employeeDao).deleteById(1L);

        assertThrows(RuntimeException.class, () -> employeeService.delete(1L));
        verify(employeeDao, times(1)).findById(id);
        verify(employeeDao, times(1)).deleteById(id);
    }
}
