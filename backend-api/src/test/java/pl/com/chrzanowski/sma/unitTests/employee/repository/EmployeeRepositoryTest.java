package pl.com.chrzanowski.sma.unitTests.employee.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.employee.model.Employee;
import pl.com.chrzanowski.sma.employee.repository.EmployeeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest extends AbstractTestContainers {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void saveAndFindById_Positive() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        Employee saved = employeeRepository.save(employee);

        // When
        Optional<Employee> found = employeeRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("Jan", found.get().getFirstName());
        assertEquals("Kowalski", found.get().getLastName());
    }

    @Test
    void findById_Negative() {
        // When
        Optional<Employee> result = employeeRepository.findById(9999L);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_Positive() {
        // Given
        Employee employee = Employee.builder()
                .firstName("Anna")
                .lastName("Nowak")
                .build();

        Employee saved = employeeRepository.save(employee);

        // When
        employeeRepository.deleteById(saved.getId());
        Optional<Employee> result = employeeRepository.findById(saved.getId());

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long nonexistentId = 123456L;

        // When
        employeeRepository.deleteById(nonexistentId);

        // Then
        Optional<Employee> result = employeeRepository.findById(nonexistentId);
        assertTrue(result.isEmpty());
    }
}
